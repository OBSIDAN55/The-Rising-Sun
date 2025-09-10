# Система гусениц для танковых юнитов

Эта система предоставляет возможность создания анимированных гусениц для танковых юнитов в Mindustry с автоматическим определением координат из изображений.

## Основные компоненты

### 1. TreadClass
Основной класс для управления отдельными гусеницами.

**Основные параметры:**
- `x, y` - координаты гусеницы
- `width, height` - размеры гусеницы
- `textureOffset` - смещение текстуры для анимации
- `animationSpeed` - скорость анимации
- `color` - цвет гусеницы
- `scale` - масштаб гусеницы

**Основные методы:**
- `update(Unit unit, float deltaTime)` - обновление анимации
- `draw(Unit unit)` - отрисовка гусеницы
- `setRegion(TextureRegion region)` - установка текстуры
- `setAnimationSpeed(float speed)` - установка скорости анимации
- `setColor(Color color)` - установка цвета

### 2. TreadDetector
Класс для автоматического определения координат гусениц из изображений.

**Основные методы:**
- `detectTreads(String unitName)` - определение гусениц из изображения
- `createTreadClasses(String unitName)` - создание TreadClass из изображения
- `loadTreadTexture(String unitName)` - загрузка текстуры гусениц

### 3. AdvancedTankUnit / TrsTankUnit
Расширенные классы танковых юнитов с поддержкой гусениц.

**Основные параметры:**
- `treads` - массив гусениц
- `autoDetectTreads` - автоматическое определение гусениц
- `treadAnimationSpeed` - скорость анимации гусениц
- `treadColor` - цвет гусениц

**Основные методы:**
- `initTreads(String unitName)` - инициализация гусениц
- `addTread(TreadClass tread)` - добавление гусеницы
- `setTreadAnimationSpeed(float speed)` - установка скорости анимации
- `setTreadColor(Color color)` - установка цвета гусениц

## Использование

### 1. Автоматическое определение гусениц

```java
TrsTankUnit unit = new TrsTankUnit();
unit.autoDetectTreads = true;
unit.initTreads("disaster"); // Ищет изображение "disaster-treads.png"
```

### 2. Ручное создание гусениц

```java
TrsTankUnit unit = new TrsTankUnit();
unit.autoDetectTreads = false;

// Создаем гусеницы вручную
TreadClass leftTread = new TreadClass(-25f, -20f, 50, 15);
TreadClass rightTread = new TreadClass(-25f, 5f, 50, 15);

unit.addTread(leftTread);
unit.addTread(rightTread);

// Загружаем текстуру
TreadDetector.setTreadTextures(unit.treads, "disaster");
```

### 3. Настройка анимации

```java
// Установка скорости анимации
unit.setTreadAnimationSpeed(1.5f);

// Установка цвета гусениц
unit.setTreadColor(Color.gray);

// Установка масштаба
unit.setTreadScale(0.8f);
```

### 4. Отладка

```java
// Показать контуры гусениц
unit.showTreadOutlines = true;

// Проверить попадание в гусеницу
boolean hit = unit.isPointInTread(x, y);

// Получить гусеницу по координатам
TreadClass tread = unit.getTreadAtPoint(x, y);
```

## Требования к изображениям

1. **Название файла:** `"название юнита-treads.png"`
2. **Расположение:** `assets/sprites/units/`
3. **Формат:** PNG с прозрачностью
4. **Содержимое:** Изображение гусениц на прозрачном фоне

## Примеры использования

### Создание юнита с автоматическими гусеницами

```java
disaster = new TankUnitType("disaster"){{
    this.constructor = TrsTankUnit::create;
    // ... другие параметры ...
    
    // Инициализируем гусеницы после создания юнита
    init = () -> {
        TrsTankUnit unit = (TrsTankUnit)constructor.get();
        unit.initTreads("disaster");
        return unit;
    };
}};
```

### Создание кастомной гусеницы

```java
TreadClass customTread = new TreadClass(-30f, -40f, 40, 20);
customTread.setColor(Color.brown);
customTread.setAnimationSpeed(2.0f);
customTread.setScale(1.2f);
```

## Особенности анимации

1. **Скорость анимации** зависит от скорости движения юнита
2. **Смещение текстуры** создает эффект движения гусениц
3. **Координаты** гусениц автоматически поворачиваются вместе с юнитом
4. **Масштабирование** позволяет изменять размер гусениц

## Отладка и диагностика

1. Включите `showTreadOutlines = true` для отображения контуров
2. Используйте `debugTreads(unit)` для вывода информации о гусеницах
3. Проверяйте логи на наличие ошибок загрузки изображений

## Производительность

- Система оптимизирована для работы с множественными гусеницами
- Анимация обновляется только при движении юнита
- Текстуры загружаются один раз и переиспользуются
