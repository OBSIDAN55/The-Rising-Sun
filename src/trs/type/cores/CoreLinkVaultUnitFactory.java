package trs.type.cores;

import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import java.lang.reflect.Field;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

public class CoreLinkVaultUnitFactory extends UnitFactory {
    public boolean coreMerge = true;

    public CoreLinkVaultUnitFactory(String name) {
        super(name);
        hasItems = true;
        solid = true;
        update = true;
        sync = true;
        destructible = true;
        separateItemCapacity = true;
        group = BlockGroup.transportation;
        flags = EnumSet.of(BlockFlag.storage);
        allowResupply = true;
        envEnabled = Env.any;
    }

    public static void incinerateEffect(Building self, Building source){
        if(Mathf.chance(0.3)){
            Tile edge = Edges.getFacingEdge(source, self);
            Tile edge2 = Edges.getFacingEdge(self, source);
            if(edge != null && edge2 != null && self.wasVisible){
                Fx.coreBurn.at((edge.worldx() + edge2.worldx())/2f, (edge.worldy() + edge2.worldy())/2f);
            }
        }
    }


    public class CoreLinkVaultUnitFactoryBuild extends UnitFactoryBuild{
        public @Nullable Building linkedCore;
        public @Nullable Unit spawnedUnit;
        public int spawnedUnitId = -1;
        public boolean hasActiveUnit;
        public float lastProgress;
        public boolean armedForSpawn;
        public int spawnBlockTicks;
        public @Nullable String expectedUnitName;
        public boolean relinkPending;

        private int factoryFlag(){
            return this.pos();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            super.acceptItem(source,item);
            return linkedCore != null ? linkedCore.acceptItem(source, item) : items.get(item) < getMaximumAccepted(item);

        }

        @Override
        public void handleItem(Building source, Item item){
            if(linkedCore != null){
                if(linkedCore.items.get(item) >= ((BuildTurretRegenGeneratorCoreBlock.BuildTurretRegenGeneratorCoreBlockBuild)linkedCore).storageCapacity){
                    incinerateEffect(this, source);
                }
                ((BuildTurretRegenGeneratorCoreBlock.BuildTurretRegenGeneratorCoreBlockBuild)linkedCore).noEffect = true;
                linkedCore.handleItem(source, item);
            }else{
                super.handleItem(source, item);
            }
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            if(proximity != null){
                for(Building other : proximity){
                    if(other instanceof CoreBlock.CoreBuild core){
                        if(((CoreLinkVaultUnitFactory)block).coreMerge){
                            linkedCore = core;
                            this.items = core.items;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public boolean shouldConsume(){
            // если отмеченный юнит ещё существует — не потребляем
            if(spawnedUnit != null && spawnedUnit.isValid() && spawnedUnit.flag() == factoryFlag()) return false;
            // проверка по сохраненному id
            if(spawnedUnitId != -1){
                final boolean[] existsById = {false};
                Groups.unit.each(u -> u.id() == spawnedUnitId && u.isValid(), u -> existsById[0] = true);
                if(existsById[0]) return false;
            }
            // если по флагу найден любой юнит — не потребляем
            final boolean[] exists = {false};
            Groups.unit.each(u -> u.team == team && u.flag() == factoryFlag(), u -> exists[0] = true);
            if(exists[0]) return false;
            // короткая блокировка после спавна
            if(spawnBlockTicks > 0) return false;
            // если ожидаемый тип задан и рядом уже есть такой юнит нашей команды — не стартуем
            if(expectedUnitName != null){
                final boolean[] typeExists = {false};
                Groups.unit.intersect(x - 48f, y - 48f, 96f, 96f, u -> {
                    if(!typeExists[0] && u.team == team && u.type != null && expectedUnitName.equals(u.type.name)) typeExists[0] = true;
                });
                if(typeExists[0]) return false;
            }
            hasActiveUnit = false;
            spawnedUnit = null;
            spawnedUnitId = -1;
            return super.shouldConsume();
        }

        @Override
        public void updateTile(){
            super.updateTile();
            float f = fraction();
            // отметить «почти готово» и зафиксировать ожидаемый тип из текущего плана
            if(!hasActiveUnit && !armedForSpawn && f > 0.95f){
                armedForSpawn = true;
                expectedUnitName = resolveCurrentPlanUnitName();
            }
            if(!hasActiveUnit && armedForSpawn && f < 0.05f){
                // прогресс цикла сбросился — юнит создан; найдём ближайшего и пометим флагом фабрики
                final Unit[] found = new Unit[1];
                Groups.unit.intersect(x - 24f, y - 24f, 48f, 48f, u -> {
                    if(found[0] == null && u.team == team && (expectedUnitName == null || (u.type != null && expectedUnitName.equals(u.type.name)))) found[0] = u;
                });
                if(found[0] != null){
                    found[0].flag(factoryFlag());
                    spawnedUnit = found[0];
                    spawnedUnitId = spawnedUnit.id();
                    hasActiveUnit = true;
                    spawnBlockTicks = 30; // ~0.5 сек
                }
                armedForSpawn = false;
            }
            // если потеряли ссылку или юнит без нашего флага — пересканировать
			if(relinkPending){
 				spawnedUnit = null;
 				final Unit[] foundByFlag = new Unit[1];
				Groups.unit.each(u -> (spawnedUnitId != -1 && u.id() == spawnedUnitId) || (u.team == team && u.flag() == factoryFlag() && (expectedUnitName == null || (u.type != null && expectedUnitName.equals(u.type.name)))), u -> { if(foundByFlag[0] == null) foundByFlag[0] = u; });
 				if(foundByFlag[0] != null){
 					spawnedUnit = foundByFlag[0];
					spawnedUnitId = spawnedUnit.id();
 					hasActiveUnit = true;
 				}else{
 					hasActiveUnit = false;
 				}
 				relinkPending = false;
 			}
            if(spawnBlockTicks > 0) spawnBlockTicks--;
            lastProgress = f;
        }

        @Override
        public void itemTaken(Item item){
            if(linkedCore != null){
                linkedCore.itemTaken(item);
            }
        }

        @Override
        public int removeStack(Item item, int amount){
            int result = super.removeStack(item, amount);

            if(linkedCore != null && team == Vars.state.rules.defaultTeam && Vars.state.isCampaign()){
                Vars.state.rules.sector.info.handleCoreItem(item, -result);
            }

            return result;
        }

        @Override
        public int getMaximumAccepted(Item item){
            super.getMaximumAccepted(item);
            return linkedCore != null ? linkedCore.getMaximumAccepted(item) : itemCapacity;
        }

        @Override
        public int explosionItemCap(){
            //when linked to a core, containers/vaults are made significantly less explosive.
            return linkedCore != null ? Math.min(itemCapacity/60, 6) : itemCapacity;
        }

        @Override
        public void drawSelect(){
            if(linkedCore != null){
                linkedCore.drawSelect();
            }
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.itemCapacity && linkedCore != null) return linkedCore.sense(sensor);
            if(sensor == LAccess.progress) return Mathf.clamp(fraction());
            return super.sense(sensor);
        }

        @Override
        public void overwrote(Seq<Building> previous){
            //only add prev items when core is not linked
            if(linkedCore == null){
                for(Building other : previous){
                    if(other.items != null && other.items != items && !(other instanceof CoreLinkVaultUnitFactoryBuild b && b.linkedCore != null)){
                        items.add(other.items);
                    }
                }

                items.each((i, a) -> items.set(i, Math.min(a, itemCapacity)));
            }
        }

        @Override
        public boolean canPickup(){
            return linkedCore == null;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.bool(hasActiveUnit);
            write.str(expectedUnitName == null ? "" : expectedUnitName);
            write.i(spawnedUnitId);
            write.bool(relinkPending);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            hasActiveUnit = read.bool();
            if(revision >= 2){
                expectedUnitName = read.str();
                if(expectedUnitName != null && expectedUnitName.length() == 0) expectedUnitName = null;
            }else{
                expectedUnitName = null;
            }
            spawnedUnitId = (revision >= 3) ? read.i() : -1;
            relinkPending = (revision >= 3) ? read.bool() : true;
            // переносим восстановление в updateTile
            spawnedUnit = null;
            armedForSpawn = false;
            spawnBlockTicks = 0;
        }

        @Override
        public byte version(){
            return 3;
        }

        private @Nullable String resolveCurrentPlanUnitName(){
            try{
                Field f = UnitFactoryBuild.class.getDeclaredField("currentPlan");
                f.setAccessible(true);
                int idx = (int)f.get(this);
                Seq<UnitFactory.UnitPlan> p = ((UnitFactory)block).plans;
                if(p != null && idx >= 0 && idx < p.size){
                    UnitType ut = p.get(idx).unit;
                    return ut == null ? null : ut.name;
                }
            }catch(Throwable ignored){
            }
            return null;
        }
    }
}
