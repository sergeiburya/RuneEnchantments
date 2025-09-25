# RuneEnchantments - Плагін для Minecraft

Версія серверу: 1.21.4

Плагін додає кастомне зачарування **"Rune: VeinSmelt"** для кайла(pickaxe):

- **AutoSmelt** — автоматична виплавка залізної або золотої руд у злитки
- **VeinMiner** — видобуток жили (в межах `3*3*3` куба) руди, що примикає до зламаного одночасно
- Команда для отримання книжки зачарування: `/rune give <player> <type>`
- За замовчуванням дозвіл на виконання команди та отримання книги зачарування має адміністратор. 

---

##  Встановлення
1. Завантажте `RuneEnchantments.jar`
2. Скопіюйте файл у папку `plugins/` вашого Paper-сервера
3. Перезапустіть сервер
4. Після першого запуску створиться файл у `plugins/RuneEnchantments/config.yml`

---

## ️ Конфігурація

Налаштуйте плагін у `config.yml`: 

```yaml
veinsmelt:
  autoSmeltEnabled: true   # Автоматична виплавка
  veinMinerEnabled: true   # Видобуток жили
  veinMinerBlockLimit: 30  # Ліміт блоків за раз

loot:
  spawnChance: 0.05        # Шанс появи в скринях (5%)
  enabledStructures:
    - "fortress"
    - "dungeon"
    - "stronghold"

