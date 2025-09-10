package trs.type;

import mindustry.type.unit.TankUnitType;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import arc.graphics.Color;

/**
 * Кастомный TankUnitType с поддержкой отслеживания здоровья для управления частями
 */
public class HealthTrackingTankUnitType extends TankUnitType {
    
    public HealthTrackingTankUnitType(String name) {
        super(name);
        this.constructor = HealthTrackingTankUnit::create;
    }
    
    @Override
    public void draw(mindustry.gen.Unit unit) {
        super.draw(unit);
        
        // Отладочная информация - рисуем квадрат для проверки работы юнита
        // Цвет меняется в зависимости от состояния системы
        Color debugColor = HealthTrackingTankUnit.getDebugColor();
        Drawf.square(unit.x, unit.y, 100f, debugColor);
        
        // Вызываем отрисовку частей и из draw(), на случай если drawBody() не вызывается
        HealthTrackingTankUnit.drawPartsBasedOnHealth(unit);
        // Дополнительный квадрат для явной индикации вызова из draw()
        Drawf.square(unit.x, unit.y, 50f, Color.blue);
    }
    
    @Override
    public void drawBody(Unit unit) { 
        super.drawBody(unit); 
        // Рисуем зеленый квадрат для проверки вызова drawBody()
        Drawf.square(unit.x, unit.y, 30f, Color.green);
        
        // Отрисовка частей в зависимости от здоровья
        HealthTrackingTankUnit.drawPartsBasedOnHealth(unit);
    }
}