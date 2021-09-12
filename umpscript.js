// [#maindev] <PROPHESSOR> 05.11.2018
// Новая версия UMP скрипта по линковке скриптов (добавил присваивание ID триггерам):
const prompt    = require('prompt-sync')();
const Thing     = require('../../structures/Thing');
const UDMFBlock = require('../../lib/oop/UDMFBlock');

const getX = x => (x * 64) + 32;
const getY = y => (32 * 64) - (y * 64) - 32;
const convertToDoomGrid = (x, y) => [getX(x), getY(y)];

const getDX = x => (x - 32) / 64;
const getDY = y => (32 * 64 - y) / 64;

module.exports = function (udmfarray) {
    console.info('# == DRRP Script Assigner == #');

    let map = null;
    do {
        try {
            const tmp = prompt('Enter DRRP UMP Script map (intro.bsp): ', 'intro.bsp');
            if (!tmp) {
                console.log('Skipped by User');
                return udmfarray;
            }
            map = require('./maps/' + tmp + '.json');
        } catch (e) {
            console.error('Can\'t open this map! Try again');
        }
    } while (!map);

    console.log('Locating the local player start...');
    let localplayerstart = null;

    for (const block of udmfarray) {
        const [btype, bdata] = block;

        if (btype === 'thing' && bdata.type === 1) {
            const x = Number(bdata.x);
            const y = Number(bdata.y);

            console.log(`Located the local player start at: (${x};${y})!`);
            localplayerstart = [x, y];
            break;
        }
    }

    if (!localplayerstart) throw new Error('Does this map contain playerstart?');

    console.log(`Original player start is at: (${map.playerstart[0]};${map.playerstart[1]})`);
    
    const origpstolocps = [getX(map.playerstart[0]), getY(map.playerstart[1])];
    console.log(`Calculating offset between ${origpstolocps} and ${localplayerstart}...`);

    const coordoffset = [origpstolocps[0] - localplayerstart[0], origpstolocps[1] - localplayerstart[1]];
    console.log(`Coordinate offset is: ${coordoffset}`);

    const thingsToAdd = [];
{
        for(let i = 0; i < map.scripts.length; i++) {
            const [x, y] = map.scripts[i];
            const script = i + 1;

            let found = false;

            for(const block of udmfarray) {
                const [btype, bdata] = block;

                if(btype === 'thing') {
                    const {x:tx, y:ty} = bdata;

                    if(getX(x) === tx && getY(x) === ty) {

                        bdata.special = 80;
                        bdata.arg0 = script;
                        console.log(`script ${i + 1}: `, x, y, tx, ty, '[ASSIGNED]');
                        found = true;
                    }
                }
            }

            if(!found) {
                thingsToAdd.push(new Thing(convertToDoomGrid(x, y), 10554, [script]));
                console.log(`script ${i + 1}: `, x, y, convertToDoomGrid(x, y), 'not found => [CREATED]');
            }
        }

        for(const thing of thingsToAdd) {
            const tmp = new UDMFBlock(thing.toArray());
            tmp[1].special = 80;
            udmfarray.push(tmp);
        }

        for(const block of udmfarray) {
            const [btype, bdata] = block;

            if(btype === 'thing') {
                const {x:tx, y:ty} = bdata;

                bdata.id = (getDX(tx) << 5) | getDY(ty);
            }
        }
    }

    return udmfarray;
};
