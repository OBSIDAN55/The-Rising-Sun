package trs.type.units;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.graphics.Layer;
import mindustry.world.blocks.units.UnitAssembler;

/**
 * Кастомный UnitAssembler с анимацией манипуляторов
 * Поддерживает поэтапную сборку юнитов с анимацией движения сварок
 */
public class AnimatedUnitAssembler extends UnitAssembler {
    
    /** Количество манипуляторов */
    public int manipulatorCount = 4;
    
    /** Скорость анимации манипуляторов */
    public float manipulatorSpeed = 1f;
    
    /** Радиус движения манипуляторов */
    public float manipulatorRadius = 8f;
    
    /** Время анимации сварки */
    public float weldingTime = 2f;
    
    /** Эффект сварки */
    public Effect weldingEffect = Fx.sparkShoot;
    
    /** Цвет искр сварки */
    public Color weldingColor = Color.orange;
    
    public AnimatedUnitAssembler(String name) {
        super(name);
    }
    
    @Override
    public void init() {
        super.init();
    }
    
    public class AnimatedUnitAssemblerBuild extends UnitAssemblerBuild {
        
        /** Состояние анимации манипуляторов */
        public ManipulatorState[] manipulators;
        
        /** Текущий этап сборки (0-1) */
        public float buildProgress = 0f;
        
        /** Время текущей анимации сварки */
        public float weldingTimer = 0f;
        
        /** Активный манипулятор для сварки */
        public int activeManipulator = 0;
        
        /** Этапы сборки юнита */
        public Seq<UnitAssemblyStage> assemblyStages;
        
        /** Текущий активный этап */
        public UnitAssemblyStage currentStage;
        
        @Override
        public void created() {
            super.created();
            initManipulators();
            initAssemblyStages();
        }
        
        private void initManipulators() {
            manipulators = new ManipulatorState[manipulatorCount];
            for (int i = 0; i < manipulatorCount; i++) {
                manipulators[i] = new ManipulatorState();
                // Размещаем манипуляторы по кругу
                float angle = (360f / manipulatorCount) * i;
                manipulators[i].baseAngle = angle;
            }
        }
        
        private void initAssemblyStages() {
            assemblyStages = new Seq<>();
        }
        
        @Override
        public void updateTile() {
            super.updateTile();
            updateManipulators();
            updateAssemblyStages();
        }
        
         private void updateManipulators() {
             if (manipulators == null) return;
             
             // Обновляем прогресс сборки
             buildProgress = progress / 100f; // Упрощенный расчет прогресса
             
             // Анимация манипуляторов
             for (int i = 0; i < manipulators.length; i++) {
                 ManipulatorState manip = manipulators[i];
                 
                 // Базовое вращение (медленное)
                 manip.baseRotation += Time.delta * manipulatorSpeed * 0.2f;
                 
                 // Анимация движения к точке сборки
                 if (buildProgress > 0f) {
                     // Плавное движение
                     float targetAngle = manip.baseAngle + Mathf.sin(Time.time * 0.5f + i) * 20f + 
                                        Mathf.cos(Time.time * 0.3f + i * 0.5f) * 10f;
                     manip.targetAngle = targetAngle;
                     
                     // Плавное движение к цели
                     float angleDiff = manip.targetAngle - manip.currentAngle;
                     if (angleDiff > 180f) angleDiff -= 360f;
                     if (angleDiff < -180f) angleDiff += 360f;
                     manip.currentAngle += angleDiff * Time.delta * 1f;
                     
                     // Анимация сварки
                     if (i == activeManipulator && buildProgress > 0.1f) {
                         weldingTimer += Time.delta;
                         if (weldingTimer >= weldingTime) {
                             weldingTimer = 0f;
                             activeManipulator = (activeManipulator + 1) % manipulatorCount;
                             
                             // Эффект сварки
                             Vec2 weldPos = getWeldPosition(manip);
                             weldingEffect.at(weldPos.x, weldPos.y, manip.currentAngle, weldingColor);
                         }
                     }
                 } else {
                     // Простая анимация в покое (очень медленная)
                     manip.currentAngle = manip.baseAngle + Mathf.sin(Time.time * 0.2f + i) * 5f;
                 }
             }
         }
        
        private void updateAssemblyStages() {
            if (assemblyStages == null) return;
            
            // Инициализируем этапы для текущего плана, если еще не сделано
            if (assemblyStages.isEmpty()) {
                // Создаем базовые этапы сборки
                assemblyStages = UnitAssemblyStage.createDefaultStages();
            }
            
            // Обновляем все этапы
            for (UnitAssemblyStage stage : assemblyStages) {
                stage.update(buildProgress);
                
                // Определяем текущий активный этап
                if (stage.isActive(buildProgress)) {
                    currentStage = stage;
                }
            }
        }
        
        private Vec2 getWeldPosition(ManipulatorState manip) {
            // Вычисляем позицию конца манипулятора
            float weldX = x + Mathf.cosDeg(manip.currentAngle) * manipulatorRadius;
            float weldY = y + Mathf.sinDeg(manip.currentAngle) * manipulatorRadius;
            return new Vec2(weldX, weldY);
        }
        
        @Override
        public void draw() {
            super.draw();
            drawManipulators();
            drawAssemblyStages();
        }
        
        private void drawManipulators() {
            if (manipulators == null) return;
            
            Draw.z(Layer.blockOver + 0.1f);
            
            for (int i = 0; i < manipulators.length; i++) {
                ManipulatorState manip = manipulators[i];
                drawManipulator(manip, i);
            }
            
            // Дополнительные эффекты
            drawWeldingEffects();
            
            Draw.reset();
        }
        
         private void drawWeldingEffects() {
             if (buildProgress <= 0f) return;
             
             // Рисуем искры сварки вокруг блока (медленно)
             for (int i = 0; i < 6; i++) {
                 if (Mathf.random() < 0.15f) {
                     float angle = Time.time * 0.5f + i * 60f;
                     float radius = manipulatorRadius + Mathf.random(2f);
                     float sparkX = x + Mathf.cosDeg(angle) * radius;
                     float sparkY = y + Mathf.sinDeg(angle) * radius;
                     
                     Draw.color(weldingColor.r, weldingColor.g, weldingColor.b, 0.6f);
                     Draw.rect(Core.atlas.find("circle"), sparkX, sparkY, 0.8f + Mathf.random(0.5f));
                     Draw.color();
                 }
             }
         }
        
        private void drawManipulator(ManipulatorState manip, int index) {
            // Упрощенная отрисовка манипуляторов с использованием базовых спрайтов
            
            // Базовый сустав в центре
            TextureRegion jointRegion = Core.atlas.find("universal-collector-units-manipulator-joint");
            if (!jointRegion.found()) {
                jointRegion = Core.atlas.find("universal-collector-units");
            }
            
            if (jointRegion.found()) {
                Draw.rect(jointRegion, x, y, manip.baseRotation);
            }
            
            // Первая часть ноги
            TextureRegion legRegion = Core.atlas.find("universal-collector-units-manipulator-leg");
            if (!legRegion.found()) {
                legRegion = Core.atlas.find("universal-collector-units");
            }
            
            if (legRegion.found()) {
                float leg1Angle = manip.currentAngle;
                float leg1X = x + Mathf.cosDeg(leg1Angle) * manipulatorRadius * 0.3f;
                float leg1Y = y + Mathf.sinDeg(leg1Angle) * manipulatorRadius * 0.3f;
                
                Draw.rect(legRegion, leg1X, leg1Y, leg1Angle);
                
                // Второй сустав
                if (jointRegion.found()) {
                    Draw.rect(jointRegion, leg1X, leg1Y, leg1Angle);
                }
                
                // Вторая часть ноги
                float leg2Angle = leg1Angle + Mathf.sin(Time.time + index) * 15f;
                float leg2X = leg1X + Mathf.cosDeg(leg2Angle) * manipulatorRadius * 0.4f;
                float leg2Y = leg1Y + Mathf.sinDeg(leg2Angle) * manipulatorRadius * 0.4f;
                
                Draw.rect(legRegion, leg2X, leg2Y, leg2Angle);
                
                // Третий сустав
                if (jointRegion.found()) {
                    Draw.rect(jointRegion, leg2X, leg2Y, leg2Angle);
                }
                
                // Рука манипулятора
                TextureRegion handRegion = Core.atlas.find("universal-collector-units-manipulator-hand");
                if (!handRegion.found()) {
                    handRegion = Core.atlas.find("universal-collector-units");
                }
                
                if (handRegion.found()) {
                    float handAngle = leg2Angle + Mathf.cos(Time.time * 2f + index) * 20f;
                    float handX = leg2X + Mathf.cosDeg(handAngle) * manipulatorRadius * 0.3f;
                    float handY = leg2Y + Mathf.sinDeg(handAngle) * manipulatorRadius * 0.3f;
                    
                    Draw.rect(handRegion, handX, handY, handAngle);
                    
                    // Эффект сварки на конце манипулятора
                    if (index == activeManipulator && weldingTimer > 0f) {
                        float sparkIntensity = Mathf.sin(weldingTimer * 20f) * 0.5f + 0.5f;
                        Draw.color(weldingColor.r, weldingColor.g, weldingColor.b, sparkIntensity);
                        
                        // Используем простой круг для искр
                        Draw.rect(Core.atlas.find("circle"), handX, handY, 2f);
                        Draw.color();
                    }
                }
            }
            
             // Если спрайты не найдены, рисуем простые линии для отладки
             if (!jointRegion.found() && !legRegion.found()) {
                 // Рисуем манипулятор как тонкие линии
                 Draw.color(Color.orange, 0.5f);
                 
                 // Основная линия манипулятора
                 float endX = x + Mathf.cosDeg(manip.currentAngle) * manipulatorRadius;
                 float endY = y + Mathf.sinDeg(manip.currentAngle) * manipulatorRadius;
                 
                 // Рисуем линию как прямоугольник
                 float lineLength = Mathf.dst(x, y, endX, endY);
                 float lineAngle = Mathf.angle(endX - x, endY - y);
                 Draw.rect(Core.atlas.find("white"), (x + endX) / 2f, (y + endY) / 2f, lineLength, 1f, lineAngle);
                 
                 // Только основные суставы
                 Draw.rect(Core.atlas.find("circle"), x, y, 1f);
                 Draw.rect(Core.atlas.find("circle"), endX, endY, 1f);
                 
                 // Эффект сварки на конце
                 if (index == activeManipulator && weldingTimer > 0f) {
                     float sparkIntensity = Mathf.sin(weldingTimer * 10f) * 0.3f + 0.7f;
                     Draw.color(weldingColor.r, weldingColor.g, weldingColor.b, sparkIntensity);
                     Draw.rect(Core.atlas.find("circle"), endX, endY, 2f);
                 }
                 
                 Draw.color();
             }
        }
        
        private void drawAssemblyStages() {
            if (assemblyStages == null) return;
            
            // Рисуем все этапы сборки
            for (UnitAssemblyStage stage : assemblyStages) {
                stage.drawPart(this, buildProgress);
            }
        }
    }
    
    /**
     * Состояние манипулятора для анимации
     */
    public static class ManipulatorState {
        /** Базовый угол размещения манипулятора */
        public float baseAngle = 0f;
        
        /** Базовое вращение */
        public float baseRotation = 0f;
        
        /** Текущий угол манипулятора */
        public float currentAngle = 0f;
        
        /** Целевой угол для плавного движения */
        public float targetAngle = 0f;
        
        /** Время анимации */
        public float animationTime = 0f;
    }
    
}
