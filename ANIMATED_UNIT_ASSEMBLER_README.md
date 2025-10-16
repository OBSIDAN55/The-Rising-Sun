# Система анимированной сборки юнитов

## Описание

Создана кастомная система сборки юнитов на основе `UnitAssembler` с продвинутой анимацией манипуляторов и поэтапной постройкой юнитов по частям.

## Компоненты системы

### 1. AnimatedUnitAssembler
**Файл:** `src/trs/type/units/AnimatedUnitAssembler.java`

Основной класс блока сборки юнитов с анимацией манипуляторов.

**Особенности:**
- 4 манипулятора, размещенные по кругу
- Анимация движения манипуляторов к точкам сборки
- Эффекты сварки с искрами
- Поэтапная сборка юнитов

**Настройки:**
- `manipulatorCount` - количество манипуляторов (по умолчанию 4)
- `manipulatorSpeed` - скорость анимации манипуляторов
- `manipulatorRadius` - радиус движения манипуляторов
- `weldingTime` - время анимации сварки
- `weldingColor` - цвет искр сварки

### 2. UnitAssemblyStage
**Файл:** `src/trs/type/units/UnitAssemblyStage.java`

Система поэтапной сборки юнитов с анимацией появления частей.

**Этапы сборки:**
1. **Основа (0-20%)** - скелет юнита
2. **Корпус (20-50%)** - основной корпус
3. **Механизмы (50-70%)** - ударные механизмы
4. **Двигатель (70-85%)** - модульный двигатель
5. **Вооружение (85-100%)** - катализатор экзакрима

**Анимации:**
- Масштабирование при появлении
- Пульсация
- Эффекты искр
- Плавное появление с прозрачностью

### 3. Структура манипуляторов

Каждый манипулятор состоит из:
- **Базовый сустав** (`manipulator-joint.png`) - точка поворота
- **Ноги** (`manipulator-leg.png`) - две части ноги с суставами
- **Рука** (`manipulator-hand.png`) - конечная часть с эффектами сварки

**Анимация движения:**
- Базовое вращение
- Движение к целевым точкам
- Плавная интерполяция углов
- Эффекты сварки на конце манипулятора

## Спрайты

Все спрайты находятся в папке:
`assets/sprites/blocks/production/payload/universal-collector-units/`

**Манипуляторы:**
- `manipulator-hand.png` - рука манипулятора
- `manipulator-joint.png` - суставы манипулятора
- `manipulator-leg.png` - ноги манипулятора

**Компоненты юнитов:**
- `units-components/detail-body.png` - корпус
- `units-components/exacrim-catalyst.png` - катализатор
- `units-components/modular-trunk-tank.png` - двигатель
- `units-components/shock-mechanism.png` - ударный механизм
- `units-components/skeleton.png` - скелет

## Использование

### В trsBlocks.java

```java
universalCollectorUnits = new AnimatedUnitAssembler("universal-collector-units"){{
    requirements(Category.units, with(Items.graphite, 15, Items.copper, 10));
    size = 6;
    
    // Настройки анимации манипуляторов
    manipulatorCount = 4;
    manipulatorSpeed = 1.5f;
    manipulatorRadius = 12f;
    weldingTime = 1.5f;
    weldingColor = Color.orange;
    
    // Планы сборки юнитов
    plans.add(
        new AssemblerUnitPlan(trsUnits.apocalypse, 60f * 160f, PayloadStack.list(...)),
        new AssemblerUnitPlan(trsUnits.disaster, 60f * 95, PayloadStack.list(...))
    );
    
    consumePower(3f);
    consumeLiquid(Liquids.cyanogen, 12f / 60f);
}};
```

## Кастомизация

### Добавление новых этапов сборки

```java
UnitAssemblyStage newStage = new UnitAssemblyStage("custom-stage", 0.3f, 0.6f);
newStage.setPartSprite("custom-part-sprite");
newStage.setPartPosition(10, 5);
newStage.setPartRotation(45f);
newStage.appearEffect = Fx.customEffect;
newStage.effectColor = Color.red;
```

### Настройка манипуляторов

```java
// В AnimatedUnitAssembler
manipulatorCount = 6; // Больше манипуляторов
manipulatorSpeed = 2f; // Быстрее анимация
manipulatorRadius = 15f; // Больший радиус
weldingTime = 2f; // Дольше сварка
weldingColor = Color.blue; // Другой цвет искр
```

## Технические детали

### Анимация манипуляторов
- Использует `ManipulatorState` для отслеживания состояния каждого манипулятора
- Плавная интерполяция углов с учетом кратчайшего пути
- Синхронизированные эффекты сварки

### Поэтапная сборка
- Автоматическое определение этапов на основе компонентов юнита
- Анимация появления с масштабированием и пульсацией
- Эффекты искр при завершении этапов

### Производительность
- Оптимизированная отрисовка с правильными слоями
- Минимальные вычисления в updateTile()
- Эффективное управление памятью

## Совместимость

Система полностью совместима с существующими планами сборки юнитов и может использоваться как замена стандартного `UnitAssembler` без изменения логики игры.

