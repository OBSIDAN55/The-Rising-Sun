package trs.type.test;

import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.blocks.production.GenericCrafter;
import trs.multicraft.IOEntry;
import trs.multicraft.Recipe;

public class TestGenericCrafter extends GenericCrafter {
    
    // Рецепты для разных режимов работы
    public Seq<Recipe> recipes = new Seq<>();
    
    public TestGenericCrafter(String name) {
        super(name);
        configurable = true;
        hasItems = true;
        // Убираем поддержку энергии, чтобы избежать ошибок
        // hasPower = true; 
        // outputsPower = true; 
        setupRecipes();
    }
    
    private void setupRecipes() {
        // Рецепт 0: Обычный режим - производство стали из железа
        Recipe normalRecipe = new Recipe();
        normalRecipe.input = new IOEntry();
        normalRecipe.input.items = new ItemStack[]{new ItemStack(Items.copper, 2)};
        normalRecipe.output = new IOEntry();
        normalRecipe.output.items = new ItemStack[]{new ItemStack(Items.graphite, 1)};
        normalRecipe.craftTime = 60f; // 1 секунда
        recipes.add(normalRecipe);
        
        // Рецепт 1: Обработка угля - потребляет уголь, производит графит
        Recipe coalRecipe = new Recipe();
        coalRecipe.input = new IOEntry();
        coalRecipe.input.items = new ItemStack[]{new ItemStack(Items.coal, 1)};
        coalRecipe.input.power = 0f; // Не потребляет энергию
        coalRecipe.output = new IOEntry();
        coalRecipe.output.items = new ItemStack[]{new ItemStack(Items.graphite, 1)};
        coalRecipe.craftTime = 30f; // 0.5 секунды
        recipes.add(coalRecipe);
        
        // Рецепт 2: Обработка песка - производит кремний из песка
        Recipe consumeRecipe = new Recipe();
        consumeRecipe.input = new IOEntry();
        consumeRecipe.input.items = new ItemStack[]{new ItemStack(Items.sand, 1)};
        consumeRecipe.input.power = 0f; // Убираем потребление энергии
        consumeRecipe.output = new IOEntry();
        consumeRecipe.output.items = new ItemStack[]{new ItemStack(Items.silicon, 1)};
        consumeRecipe.craftTime = 90f; // 1.5 секунды
        recipes.add(consumeRecipe);
        
        // Кэшируем уникальные элементы для каждого рецепта
        for (Recipe recipe : recipes) {
            recipe.cacheUnique();
        }
    }

    public class TestGenericCrafterBuild extends GenericCrafterBuild {

        int selectedMode = 0; // По умолчанию обычный режим
        float craftingTime = 0f;

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
                craftingTime = 0f; // Сбрасываем время крафта при смене режима
            });
            button1.update(() -> button1.setChecked(selectedMode == 0));
            buttonTable.add(button1);

            // Кнопка 2 - Обработка угля
            TextButton button2 = new TextButton("Уголь", Styles.togglet);
            button2.clicked(() -> {
                selectedMode = 1;
                configure(1);
                craftingTime = 0f; // Сбрасываем время крафта при смене режима
            });
            button2.update(() -> button2.setChecked(selectedMode == 1));
            buttonTable.add(button2);

            // Кнопка 3 - Обработка песка
            TextButton button3 = new TextButton("Песок", Styles.togglet);
            button3.clicked(() -> {
                selectedMode = 2;
                configure(2);
                craftingTime = 0f; // Сбрасываем время крафта при смене режима
            });
            button3.update(() -> button3.setChecked(selectedMode == 2));
            buttonTable.add(button3);

            table.add(buttonTable);
            
            // Добавляем информацию о текущем рецепте
            table.row();
            Table recipeInfo = new Table();
            recipeInfo.background(Styles.black6);
            recipeInfo.defaults().pad(8f);
            
            Recipe currentRecipe = getCurrentRecipe();
            
            // Показываем входные ресурсы
            recipeInfo.add("Вход: ").left();
            for (ItemStack input : currentRecipe.input.items) {
                recipeInfo.add(input.item.emoji() + " " + input.amount + " ").left();
            }
            
            recipeInfo.row();
            
            // Показываем выходные ресурсы
            recipeInfo.add("Выход: ").left();
            for (ItemStack output : currentRecipe.output.items) {
                recipeInfo.add(output.item.emoji() + " " + output.amount + " ").left();
            }
            
            recipeInfo.row();
            recipeInfo.add("Время: " + (currentRecipe.craftTime / 60f) + "с").left();
            
            table.add(recipeInfo).growX();
        }
        
        // Получаем текущий рецепт на основе выбранного режима
        public Recipe getCurrentRecipe() {
            if (selectedMode >= 0 && selectedMode < recipes.size) {
                return recipes.get(selectedMode);
            }
            return recipes.get(0); // Возвращаем первый рецепт по умолчанию
        }
        
        @Override
        public void updateTile() {
            super.updateTile();
            
            Recipe currentRecipe = getCurrentRecipe();
            
            // Проверяем, можем ли мы производить
            if (canProduce(currentRecipe)) {
                // Обновляем время крафта
                craftingTime += delta();
                
                // Если время крафта достигло необходимого, производим
                if (craftingTime >= currentRecipe.craftTime) {
                    produce(currentRecipe);
                    craftingTime = 0f; // Сбрасываем время крафта
                }
            } else {
                craftingTime = 0f; // Сбрасываем время крафта если не можем производить
            }
        }
        
        // Проверяем, можем ли мы производить с текущим рецептом
        private boolean canProduce(Recipe recipe) {
            // Проверяем входные предметы
            for (ItemStack input : recipe.input.items) {
                if (items.get(input.item) < input.amount) {
                    return false;
                }
            }
            
            // Проверяем, есть ли место для выходных предметов
            for (ItemStack output : recipe.output.items) {
                if (items.get(output.item) + output.amount > itemCapacity) {
                    return false;
                }
            }
            
            return enabled;
        }
        
        // Производим согласно рецепту
        private void produce(Recipe recipe) {
            // Потребляем входные предметы
            for (ItemStack input : recipe.input.items) {
                items.remove(input.item, input.amount);
            }
            
            // Производим выходные предметы
            for (ItemStack output : recipe.output.items) {
                items.add(output.item, output.amount);
            }
        }
        
        // Получаем прогресс крафта (0.0 - 1.0)
        public float getProgress() {
            Recipe currentRecipe = getCurrentRecipe();
            if (currentRecipe.craftTime <= 0) return 1f;
            return Math.min(craftingTime / currentRecipe.craftTime, 1f);
        }
        
        
        // Переопределяем метод progress для отображения в UI
        @Override
        public float progress() {
            return getProgress();
        }

        @Override
        public void write(arc.util.io.Writes write) {
            super.write(write);
            write.i(selectedMode);
            write.f(craftingTime);
        }

        @Override
        public void read(arc.util.io.Reads read, byte revision) {
            super.read(read, revision);
            selectedMode = read.i();
            craftingTime = read.f();
        }
}
}
