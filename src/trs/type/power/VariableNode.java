package trs.type.power;

import static trs.type.Vars.*;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.graphics.Pal;
import mindustry.ui.*;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class VariableNode extends PowerNode {

    public float baseLaserRange = 6;
    public int baseMaxNodes = 10;

    public float farLaserRange = 60;
    public int farMaxNodes = 3;

    public float closeLaserRange = 3;
    int closeMaxNodes = 99999999;


    public float powerGeneration = 50f;

    public VariableNode(String name) {
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
        private boolean modeInitialized = false; // Флаг инициализации режима

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
            
            // Инициализируем режим при первом запуске
            if (!modeInitialized) {
                modeInitialized = true;
                selectedMode = 0; // Устанавливаем режим по умолчанию
                applyModeSettings();
                // Отладочная информация

            }
        }
        
        @Override
        public void draw() {
            super.draw();
            
            // Отладочная информация
            drawPlaceText("Режим: " + selectedMode, (int)x, (int)y, true);
            
            // Визуальные индикаторы для разных режимов
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
            
            // Кнопка 1 - Обычный режим (нейтральный)
            TextButton button1 = new TextButton("Обычный", Styles.togglet);
            button1.clicked(() -> {
                selectedMode = 0;
                configure(0);
                // Сбрасываем все подключения при смене режима
                power.links.clear();
            });
            button1.update(() -> button1.setChecked(selectedMode == 0));
            buttonTable.add(button1);
            
            // Кнопка 2 - Генерация энергии
            TextButton button2 = new TextButton("Генерация", Styles.togglet);
            button2.clicked(() -> {
                selectedMode = 1;
                configure(1);
                // Сбрасываем все подключения при смене режима
                power.links.clear();
            });
            button2.update(() -> button2.setChecked(selectedMode == 1));
            buttonTable.add(button2);
            
            // Кнопка 3 - Потребление энергии
            TextButton button3 = new TextButton("Потребление", Styles.togglet);
            button3.clicked(() -> {
                selectedMode = 2;
                configure(2);
                // Сбрасываем все подключения при смене режима
                power.links.clear();
            });
            button3.update(() -> button3.setChecked(selectedMode == 2));
            buttonTable.add(button3);
            
            table.add(buttonTable);
        }

        @Override
        public void configure(Object value) {
            super.configure(value);

            
            // Отладочная информация
            drawPlaceText("configure: " + value + ", режим: " + selectedMode, (int)x, (int)y, true);
            
            if (value instanceof Integer) {
                int newMode = (Integer) value;
                // Проверяем, изменился ли режим
                if (newMode != selectedMode) {
                    selectedMode = newMode;
                    // Применяем настройки режима
                    applyModeSettings();
                    drawPlaceText("Режим изменен на " + selectedMode, (int)x, (int)y, true);
                }
            } else if (value == null && !modeInitialized) {
                // Если значение null и режим не инициализирован, восстанавливаем режим по умолчанию
                selectedMode = 0;
                applyModeSettings();
                drawPlaceText("Режим восстановлен: " + selectedMode, (int)x, (int)y, true);
            }
        }
        
        private void applyModeSettings() {
            switch (selectedMode) {
                case 0: // Обычный режим
                    maxNodes = baseMaxNodes;
                    laserRange = baseLaserRange;
                    addBar("connections", entity -> new Bar(() ->
                            Core.bundle.format("bar.powerlines", entity.power.links.size, maxNodes),
                            () -> Pal.items,
                            () -> (float)entity.power.links.size / (float)maxNodes
                    ));
                    break;
                case 1: // Генерация
                    maxNodes = farMaxNodes;
                    laserRange = farLaserRange;
                    addBar("connections", entity -> new Bar(() ->
                            Core.bundle.format("bar.powerlines", entity.power.links.size, maxNodes),
                            () -> Pal.items,
                            () -> (float)entity.power.links.size / (float)maxNodes
                    ));
                    break;
                case 2: // Потребление
                    maxNodes = closeMaxNodes;
                    laserRange = closeLaserRange;
                    removeBar("connections");
                    break;
            }
            
            // Принудительно обновляем подключения для обновления лазеров
            if (power != null) {
                power.links.clear();
                // Переподключаем все узлы в радиусе
                for (mindustry.gen.Building other : proximity) {
                    if (other instanceof PowerNodeBuild && other != this) {
                        PowerNodeBuild otherNode = (PowerNodeBuild) other;
                        if (otherNode.power != null && power.links.size < maxNodes) {
                            power.links.add(otherNode.pos());
                            otherNode.power.links.add(pos());
                        }
                    }
                }
            }
        }
        
        @Override
        public void write(arc.util.io.Writes write) {
            super.write(write);
            write.i(selectedMode);
            write.bool(modeInitialized);
        }
        
        @Override
        public void read(arc.util.io.Reads read, byte revision) {
            super.read(read, revision);
            selectedMode = read.i();
            if (revision >= 1) {
                modeInitialized = read.bool();
            } else {
                modeInitialized = true; // Для старых сохранений
            }
        }
    }
}
