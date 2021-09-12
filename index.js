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
        console.info("# === OE2RP Script Linker === #");

        if(args.length != 3) {
            console.info("Usage: <path/to/file.bsp>");
            return 1;
        }

        const bsp = fs.readFileSync(args[2]);

        const file = new ByteTools(bsp);

        console.log(`Version: ${file.readUInt8()}`);
        console.log(`Creation date: ${new Date(file.readUInt32LE() + 1000)}`);
        console.log(`Unknown 1: ${file.readUInt16LE()}`);
        console.log(`Floor   color: rgb(${file.readUInt8()}, ${file.readUInt8()}, ${file.readUInt8()})`);
        console.log(`Ceiling color: rgb(${file.readUInt8()}, ${file.readUInt8()}, ${file.readUInt8()})`);
        console.log(`Loading color: rgb(${file.readUInt8()}, ${file.readUInt8()}, ${file.readUInt8()})`);
        console.log(`Level id: ${file.readUInt8()}`);

        const playerstart = file.readUInt16LE();
        console.log(`Player start: (${playerstart % 32};${~~(playerstart / 32)})`);

        console.log(`Player rotation: ${file.readUInt8()}`);
        
        console.log(`Unknown 2: ${file.readInt32LE()}`);
        console.log(`Unknown 3: ${file.readUInt8()}`);

        const bspNodes = file.readUInt16LE();

        file.seek(bspNodes * 10, 'CUR');
        console.log(`Skipped ${bspNodes} BSP nodes to: ${file.tell()}`);

        const lines = file.readUInt16LE();

        file.seek(lines * 10, 'CUR');
        console.log(`Skipped ${lines} lines to: ${file.tell()}`);

        const things = file.readUInt16LE();
        const extraThings = file.readUInt16LE();

        file.seek(things * 5, 'CUR');
        console.log(`Skipped ${things} things to: ${file.tell()}`);

        file.seek(things * 7, 'CUR');
        console.log(`Skipped ${things} extra things to: ${file.tell()}`);

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
