package trs.type.power;

import static trs.type.Vars.*;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Point2;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.Scaling;
import mindustry.graphics.Pal;
import mindustry.ui.*;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class LargeVariableNode extends PowerNode {

    public float baseLaserRange = 6;
    public int baseMaxNodes = 10;

    public float farLaserRange = 60;
    public int farMaxNodes = 3;

    public float closeLaserRange = 3;
    int closeMaxNodes = 2147483647;


    public float powerGeneration = 50f;

    public LargeVariableNode(String name) {
        super(name);
        configurable = true;
        saveConfig = true;
        consumesPower = false;
        outputsPower = true;
        hasPower = true;
    }
    public void setBars(){
        super.setBars();
            removeBar("connections");
            
    }
    @Override
    public void setStats(){
        super.setStats();

        stats.remove(Stat.powerRange);
        stats.remove(Stat.powerConnections);

        stats.add(basePowerRange,baseLaserRange, StatUnit.blocks);
        stats.add(basePowerConnections,baseMaxNodes, StatUnit.none);

        stats.add(farPowerRange,farLaserRange, StatUnit.blocks);
        stats.add(farPowerConnections,farMaxNodes, StatUnit.none);
        stats.add(farModePowerGeneration,powerGeneration * 60.0f, StatUnit.powerSecond);

        stats.add(closePowerRange,closeLaserRange, StatUnit.blocks);
        stats.add(closePowerConnections,"∞", StatUnit.none);
    }

    public class VariableNodeBuild extends PowerNodeBuild {
        public int selectedMode = 0; // 0, 1, 2 для трех режимов

        @Override
        public float getPowerProduction() {
            // Генерируем энергию только в режиме 1
            if (selectedMode == 1) {
                return powerGeneration;
            }
            return 0f;
        }
        
        @Override
        public void updateTile() {
            super.updateTile();
        }
        
        @Override
        public void draw() {
            super.draw();
            if (selectedMode == 1) {
                // Зеленый цвет для режима генерации
                Draw.color(Pal.accent);
                Draw.alpha(0.3f);
                Fill.circle(x, y, size * 8f);
                Draw.reset();
            } else if (selectedMode == 2) {
                // Красный цвет для режима потребления
                Draw.color(Pal.remove);
                Draw.alpha(0.3f);
                Fill.circle(x, y, size * 8f);
                Draw.reset();
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.background(Styles.black6);
            
            // Создаем таблицу с тремя кнопками
            Table buttonTable = new Table();
            buttonTable.defaults().size(80f, 50f);
            buttonTable.defaults().scaling(Scaling.fill);
            
            // Кнопка 1 - Обычный режим (нейтральный)
            TextButton button1 = new TextButton("\uE853", Styles.togglet);
            button1.clicked(() -> {
                selectedMode = 0;

                maxNodes = baseMaxNodes;
                laserRange = baseLaserRange;
                addBar("connections", entity -> new Bar(() ->
                        Core.bundle.format("bar.powerlines", entity.power.links.size, maxNodes),
                        () -> Pal.items,
                        () -> (float)entity.power.links.size / (float)maxNodes
                ));

                // Сбрасываем все подключения при смене режима
                configure(new Point2[0]);
            });
            button1.update(() -> button1.setChecked(selectedMode == 0));
            buttonTable.add(button1);
            
            // Кнопка 2 - Генерация энергии
            TextButton button2 = new TextButton("\uE844", Styles.togglet);
            button2.clicked(() -> {
                selectedMode = 1;

                maxNodes = farMaxNodes;
                laserRange = farLaserRange;
                addBar("connections", entity -> new Bar(() ->
                        Core.bundle.format("bar.powerlines", entity.power.links.size, maxNodes),
                        () -> Pal.items,
                        () -> (float)entity.power.links.size / (float)maxNodes
                ));
                // Сбрасываем все подключения при смене режима
                configure(new Point2[0]);
            });
            button2.update(() -> button2.setChecked(selectedMode == 1));
            buttonTable.add(button2);
            
            // Кнопка 3 - Потребление энергии
            TextButton button3 = new TextButton("\uE86C", Styles.togglet);
            button3.clicked(() -> {
                selectedMode = 2;

                maxNodes = closeMaxNodes;
                laserRange = closeLaserRange;
                removeBar("connections");
                // Сбрасываем все подключения при смене режима
                configure(new Point2[0]);

            });
            button3.update(() -> button3.setChecked(selectedMode == 2));
            buttonTable.add(button3);
            
            table.add(buttonTable);
        }
        
        @Override
        public void write(arc.util.io.Writes write) {
            super.write(write);
            write.i(selectedMode);
        }
        
        @Override
        public void read(arc.util.io.Reads read, byte revision) {
            super.read(read, revision);
            selectedMode = read.i();
        }
    }
}
