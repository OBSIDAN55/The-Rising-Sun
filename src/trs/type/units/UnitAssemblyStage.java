package trs.type.units;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.type.PayloadStack;
import mindustry.type.UnitType;
import mindustry.world.Block;

/**
 * Система поэтапной сборки юнитов
 * Определяет этапы сборки и анимацию появления частей
 */
public class UnitAssemblyStage {
    
    /** Имя этапа */
    public String name;
    
    /** Прогресс начала этапа (0-1) */
    public float startProgress;
    
    /** Прогресс окончания этапа (0-1) */
    public float endProgress;
    
    /** Компоненты для этого этапа */
    public Seq<PayloadStack> components;
    
    /** Спрайт части юнита для отображения */
    public String partSprite;
    
    /** Позиция части относительно центра сборки */
    public float partX, partY;
    
    /** Угол поворота части */
    public float partRotation;
    
    /** Эффект появления части */
    public Effect appearEffect = Fx.sparkShoot;
    
    /** Цвет эффекта */
    public Color effectColor = Color.cyan;
    
    /** Время анимации появления */
    public float appearTime = 1f;
    
    /** Анимация появления завершена */
    public boolean appeared = false;
    
    /** Время анимации */
    public float animationTime = 0f;
    
    public UnitAssemblyStage(String name, float startProgress, float endProgress) {
        this.name = name;
        this.startProgress = startProgress;
        this.endProgress = endProgress;
        this.components = new Seq<>();
    }
    
    /**
     * Добавляет компонент к этапу
     */
    public UnitAssemblyStage addComponent(Block block, int amount) {
        components.add(new PayloadStack(block, amount));
        return this;
    }
    
    /**
     * Устанавливает спрайт части
     */
    public UnitAssemblyStage setPartSprite(String sprite) {
        this.partSprite = sprite;
        return this;
    }
    
    /**
     * Устанавливает позицию части
     */
    public UnitAssemblyStage setPartPosition(float x, float y) {
        this.partX = x;
        this.partY = y;
        return this;
    }
    
    /**
     * Устанавливает угол поворота части
     */
    public UnitAssemblyStage setPartRotation(float rotation) {
        this.partRotation = rotation;
        return this;
    }
    
    /**
     * Проверяет, активен ли этап на данном прогрессе
     */
    public boolean isActive(float progress) {
        return progress >= startProgress && progress <= endProgress;
    }
    
    /**
     * Проверяет, должен ли этап появиться
     */
    public boolean shouldAppear(float progress) {
        return progress >= startProgress && !appeared;
    }
    
    /**
     * Обновляет анимацию этапа
     */
    public void update(float progress) {
        if (shouldAppear(progress)) {
            appeared = true;
            animationTime = 0f;
        }
        
        if (appeared && animationTime < appearTime) {
            animationTime += Time.delta;
        }
    }
    
    /**
     * Получает прогресс анимации появления (0-1)
     */
    public float getAppearProgress() {
        if (!appeared) return 0f;
        return Mathf.clamp(animationTime / appearTime);
    }
    
    /**
     * Рисует часть юнита
     */
    public void drawPart(Building build, float buildProgress) {
        if (!appeared || partSprite == null) return;
        
        float appearProgress = getAppearProgress();
        if (appearProgress <= 0f) return;
        
        TextureRegion region = Core.atlas.find(partSprite);
        if (!region.found()) return;
        
        Draw.z(Layer.blockOver + 0.1f);
        
        // Анимация появления (масштабирование)
        float scale = Mathf.lerp(0.1f, 1f, appearProgress);
        
        // Пульсация
        float pulse = Mathf.sin(Time.time * 5f) * 0.1f + 1f;
        scale *= pulse;
        
        // Применяем масштабирование
        Draw.scl(scale);
        
        // Позиция с учетом анимации
        float drawX = build.x + partX;
        float drawY = build.y + partY;
        
        // Эффект появления
        if (appearProgress < 1f) {
            float alpha = appearProgress;
            Color prev = Draw.getColor();
            Draw.color(prev.r, prev.g, prev.b, alpha);
        }
        
        // Рисуем часть
        Draw.rect(region, drawX, drawY, partRotation);
        
        // Сбрасываем масштабирование
        Draw.scl(1f);
        
        // Эффект искр
        if (appearProgress > 0.8f && Mathf.random() < 0.1f) {
            appearEffect.at(drawX + Mathf.random(-8f, 8f), drawY + Mathf.random(-8f, 8f), 
                           partRotation, effectColor);
        }
        
        Draw.reset();
    }
    
    /**
     * Создает этапы сборки для юнита на основе его компонентов
     */
    public static Seq<UnitAssemblyStage> createStagesForUnit(UnitType unit, Seq<PayloadStack> components) {
        Seq<UnitAssemblyStage> stages = new Seq<>();
        
        // Этап 1: Основа (0-0.2)
        UnitAssemblyStage foundation = new UnitAssemblyStage("foundation", 0f, 0.2f);
        foundation.setPartSprite("units-components/skeleton");
        foundation.setPartPosition(0, 0);
        stages.add(foundation);
        
        // Этап 2: Корпус (0.2-0.5)
        UnitAssemblyStage body = new UnitAssemblyStage("body", 0.2f, 0.5f);
        body.setPartSprite("units-components/detail-body");
        body.setPartPosition(0, 0);
        PayloadStack bodyStack = components.find(c -> c.item.name.equals("detail-body"));
        if (bodyStack != null) {
            body.addComponent((Block)bodyStack.item, bodyStack.amount);
        }
        stages.add(body);
        
        // Этап 3: Механизмы (0.5-0.7)
        UnitAssemblyStage mechanisms = new UnitAssemblyStage("mechanisms", 0.5f, 0.7f);
        mechanisms.setPartSprite("units-components/shock-mechanism");
        mechanisms.setPartPosition(-8, 0);
        PayloadStack mechanismStack = components.find(c -> c.item.name.equals("shock-mechanism"));
        if (mechanismStack != null) {
            mechanisms.addComponent((Block)mechanismStack.item, mechanismStack.amount);
        }
        stages.add(mechanisms);
        
        // Этап 4: Двигатель (0.7-0.85)
        UnitAssemblyStage engine = new UnitAssemblyStage("engine", 0.7f, 0.85f);
        engine.setPartSprite("units-components/modular-trunk-tank");
        engine.setPartPosition(8, 0);
        PayloadStack engineStack = components.find(c -> c.item.name.equals("modular-trunk-tank"));
        if (engineStack != null) {
            engine.addComponent((Block)engineStack.item, engineStack.amount);
        }
        stages.add(engine);
        
        // Этап 5: Вооружение (0.85-1.0)
        UnitAssemblyStage weapons = new UnitAssemblyStage("weapons", 0.85f, 1.0f);
        weapons.setPartSprite("units-components/exacrim-catalyst");
        weapons.setPartPosition(0, -8);
        PayloadStack weaponStack = components.find(c -> c.item.name.equals("exacrim-catalyst"));
        if (weaponStack != null) {
            weapons.addComponent((Block)weaponStack.item, weaponStack.amount);
        }
        stages.add(weapons);
        
        return stages;
    }
    
    /**
     * Создает базовые этапы сборки по умолчанию
     */
    public static Seq<UnitAssemblyStage> createDefaultStages() {
        Seq<UnitAssemblyStage> stages = new Seq<>();
        
        // Этап 1: Основа (0-0.2)
        UnitAssemblyStage foundation = new UnitAssemblyStage("foundation", 0f, 0.2f);
        foundation.setPartSprite("units-components/skeleton");
        foundation.setPartPosition(0, 0);
        stages.add(foundation);
        
        // Этап 2: Корпус (0.2-0.5)
        UnitAssemblyStage body = new UnitAssemblyStage("body", 0.2f, 0.5f);
        body.setPartSprite("units-components/detail-body");
        body.setPartPosition(0, 0);
        stages.add(body);
        
        // Этап 3: Механизмы (0.5-0.7)
        UnitAssemblyStage mechanisms = new UnitAssemblyStage("mechanisms", 0.5f, 0.7f);
        mechanisms.setPartSprite("units-components/shock-mechanism");
        mechanisms.setPartPosition(-8, 0);
        stages.add(mechanisms);
        
        // Этап 4: Двигатель (0.7-0.85)
        UnitAssemblyStage engine = new UnitAssemblyStage("engine", 0.7f, 0.85f);
        engine.setPartSprite("units-components/modular-trunk-tank");
        engine.setPartPosition(8, 0);
        stages.add(engine);
        
        // Этап 5: Вооружение (0.85-1.0)
        UnitAssemblyStage weapons = new UnitAssemblyStage("weapons", 0.85f, 1.0f);
        weapons.setPartSprite("units-components/exacrim-catalyst");
        weapons.setPartPosition(0, -8);
        stages.add(weapons);
        
        return stages;
    }
}
