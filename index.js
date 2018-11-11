/**
 * Copyright (c) 2018 DRRP-Team
 * 
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

const fs = require('fs');

const ByteTools = require('./ByteTools');

class App {
    static main(args) {
        console.info("# === DRRP Script Linker === #");

        if(args.length != 3) {
            console.info("Usage: <path/to/file.bsp>");
            return 1;
        }

        const bsp = fs.readFileSync(args[2]);

        const file = new ByteTools(bsp);

        console.log(`Floor   color: rgb(${file.readUInt8()}, ${file.readUInt8()}, ${file.readUInt8()})`);
        console.log(`Ceiling color: rgb(${file.readUInt8()}, ${file.readUInt8()}, ${file.readUInt8()})`);
        console.log(`Loading color: rgb(${file.readUInt8()}, ${file.readUInt8()}, ${file.readUInt8()})`);
        console.log(`Level id: ${file.readUInt8()}`);

        const playerstart = file.readUInt16LE();
        console.log(`Player start: (${playerstart % 32};${~~(playerstart / 32)})`);

        console.log(`Player rotation: ${file.readUInt8()}`);

        file.seek(file.readUInt16LE() * 10, 'CUR');
        console.log(`Skipped to: ${file.tell()}`);

        file.seek(file.readUInt16LE() * 8, 'CUR');
        console.log(`Skipped to: ${file.tell()}`);

        file.seek(file.readUInt16LE() * 5, 'CUR');
        console.log(`Skipped to: ${file.tell()}`);

        const scripts = [];

        {
            const count = file.readInt16LE();

            console.log("# " + count + " scripts found");

            console.log("[SCRIPTS]");
            for (let i = 0; i < count; i++) {
                const packedInt = file.readInt32LE();

                const x = (packedInt & 0x1F);
                const y = ((packedInt & 0x3E0) >> 5);
                scripts.push([x, y]);
                console.log(`Script ${i} (${x};${y})`);
            }
            console.log();
        }

        const out = {
            playerstart: [playerstart % 32, ~~(playerstart / 32)],
            scripts: scripts
        };

        fs.writeFileSync(`${args[2]}.json`, JSON.stringify(out, 0, 4));
        console.info(`UMP Map ${args[2]}.json generated successfuly!`);
    }
}


App.main(process.argv);
