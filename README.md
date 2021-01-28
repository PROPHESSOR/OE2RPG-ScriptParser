# ScriptParser

Doom RPG script to ACS converter

> *[Extraction from DRRP Wiki](https://github.com/DRRP-Team/DRRP/wiki/rus-devtools)*

### Script Decompiler

- Разработчики: UsernameAK
- Язык программирования: Java
- Запускать с помощью: `mvn package && java -jar target/scriptdecompiler.jar <input.bsp> >> output.acs`

Программа, написанная в 2018 году, позволяющая конвертировать скрипты на картах из оригинального формата Doom RPG в ACS. Ранние ее версии лишь дизассемблировали скрипты.

### Script Linker

- Разработчики: PROPHESSOR
- Язык программирования: JavaScript
- Запускать с помощью: `node index.js <input.bsp>`

Написанная в 2018 году, эта программа выдаёт список координат на карте (в сетке Doom RPG) и привязанный к ним номер скрипта. Сам же скрипт генерируется с помощью [scriptdecompiler](#scriptdecompiler). Используется в [UMP Script Assigner](#scriptassigner).

### UMP Script Assigner

- Разработчики: PROPHESSOR
- Язык программирования: JavaScript (UDMF Map Processor API)
- Запускать с помощью: [(UMP) UDMF Map Processor](https://github.com/PROPHESSOR/udmfMapProcessor)

Скрипт для UMP, позволяющий в автоматическом режиме добавлять на UDMF карту триггеры, присваивать диалоги и скрипты нужным Actor'ам на карте. Использует для своей работы карты, сгенерированные в [Script Linker](#scriptlinker) (карты для оригинальных Doom RPG уровней уже встроены в скрипт).
