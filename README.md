# RuneEnchantments - Плагін для Minecraft

Версія серверу: 1.21.4

Плагін додає кастомне зачарування **"Rune: VeinSmelt"** для кайла:

- **AutoSmelt** — автоматична виплавка руд у злитки
- **VeinMiner** — видобуток всієї жили руди одночасно

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

