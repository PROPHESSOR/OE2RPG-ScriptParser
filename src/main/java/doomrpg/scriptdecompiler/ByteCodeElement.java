package doomrpg.scriptdecompiler;

import java.util.List;

public class ByteCodeElement {
	int cmdid;
	int arg1;
	int arg2;
	
	private int lookupEventVar() {
		int in = (((short) ((arg2 & 0xFFFF0000) >> 16)) & 0xFFFF);
		if(in == 0) return 0;
		int i = 0;
		while((1 << (i++)) != in && i < 31);
		return i;
	}

    private String getThingName(int id, int dead) {
        switch(id) {
            // Case list by PROPHESSOR
            case 1:  return "DRRPAxe";
            case 2:  return "FireExt";
            case 3:  return "Shotgun";
            case 4:  return "SuperShotgun";
            case 5:  return "Chaingun";
            case 6:  return "RocketLauncher";
            case 7:  return "PlasmaRifle";
            case 8:  return "BFG9000";
            case 17: return "ZombiePvt"; 
            case 18: return "ZombieLt"; 
            case 19: return "ZombieCpt"; 
            case 20: return "Hellhound"; 
            case 21: return "Cerberus"; 
            case 22: return "DemonWolf"; 
            case 23: return "Troop"; 
            case 24: return "Commando"; 
            case 25: return "Assassin"; 
            case 26: return "Impling"; 
            case 27: return "DRRPImp"; 
            case 28: return "ImpLord"; 
            case 29: return "Phantom"; 
            case 30: return "DRRPLostSoul"; 
            case 31: return "Nightmare"; 
            case 32: return "BullDemon"; 
            case 33: return "Pinky"; 
            case 34: return "Belphegor"; 
            case 35: return "Malwrath"; 
            case 36: return "DRRPCacodemon"; 
            case 37: return "Wretched"; 
            case 38: return "Beholder"; 
            case 39: return "Rahovart"; 
            case 40: return "DRRPPainElemental"; 
            case 41: return "Ghoul"; 
            case 42: return "Fiend"; 
            case 43: return "DRRPRevenant"; 
            case 44: return "Behemoth"; 
            case 45: return "Mancubus"; 
            case 46: return "Druj"; 
            case 47: return "Infernis"; 
            case 48: return "DRRPArchVile"; 
            case 49: return "Apollyon"; 
            case 50: return "Ogre"; 
            case 51: return "DRRPHellKnight"; 
            case 52: return "DRRPBaronOfHell"; 
            case 53: return "DRRPCyberdemon"; 
            case 54: return "Kronos";
            case 62: return "DRRPBurnedSteelWall";
            case 63: return "DRRPBrokenTubeWall";
            case 65: return "DRRPRedCard";
            case 66: return "DRRPBlueCard";
            case 67: return "DRRPGreenCard";
            case 71: return "DRRPWall";
            case 73: return "DRRPYellowCard";
            case 81: return "Balloon";
            case 82: return "Balloons";
            case 83: return "Cell";
            case 84: return "Cells";
            case 85: return "Shell";
            case 86: return "Shells";
            case 87: return "RocketAmmo";
            case 88: return "RocketBox";
            case 89: return "Cell";
            case 90: return "Cells";
            case 91: return "DRRPHealthBonus";
            case 92: return "DRRPArmorBonus";
            case 93: return "DRRPGreenArmor";
            case 94: return "DRRPBlueArmor";
            case 95: return "Credit";
            case 96: return "GoldCredit";
            case 99: return "SmallMedikit";
            case 100:return "LargeMedikit";
            case 101:return "InventorySoulsphere";
            case 102:return "Berserker";
            case 110:return "DogCollar";
            case 128:return "DRRPLava";
            case 129:return "DRRPFire";
            case 130:return "DRRPBarrel";
            case 131:return "Rod";
            case 132:return "Sink";
            case 133:return "Toilet";
            case 134:return "DRRPChair";
            case 135:return "YellowLamp";
            case 138:return "Exit";
            case 139:return "Wardrobe";
            case 140:return "BlueLamp";
            case 141:return "Table";
            case 142:return "Bed";

            case 145:return "Civilian";
            case 146:return "Scientist";
            case 147:return "Marine";
            case 148:return "Scientist1";
            case 149:return "DrGuerard";
            case 150:return "Civilian1"; // Nadira
            case 151:return "Civilian2";
            case 152:return "Marine1";
            // TODO: Add remaining decals
            default: return "DRRPNull";
        }
    }
	
	public String toACS(List<EventDef> lookupList, CoordList clist, List<Thing> things) {
		String theIf = "";
		theIf += "if(arg2 == " + lookupEventVar() + ") ";
		switch(cmdid) {
		case 26:
		case 8:
			return theIf + "ACS_NamedExecuteWait(\"window\", 0, getString(" + arg1 + "));";
		case 9:
			return theIf + "GiveInventory(\"MapRevealer\", 1);";
		case 22:
		case 21:
		case 23:
		{
			String statType = "ERROR";
			switch (arg1 & 0xFF) {
			case 0:
				statType = "Health";
			break;
			case 1:
				statType = "Armor";
			break;
			case 2:
				statType = "Credit";
				break;
			}
			switch(cmdid) {
			case 22:
			case 21:
				return theIf + (cmdid == 22 ? "Take" : "Give") + "Inventory(\"" + statType + "\", " + ((arg1 >> 8) & 0xFF) + ");";
			case 23:
				return theIf + "if(CheckInventory(\"" + statType + "\") < " + ((arg1 >> 8) & 0xFF) + ") { ACS_NamedExecuteWait(\"window\", 0, getString(" + (((arg1 >> 16) & 0xFFFF)) + ")); terminate; }";
			}
		}
		case 19: 
			return theIf + "ScriptCall(\"ConversationController\", \"SetArgument\", sgenid" + (clist.getPos((arg1 & 0xFF), (arg1 >> 8 & 0xFF))) + ", arg2 + 1);";
		case 11: 
			return theIf + "ScriptCall(\"ConversationController\", \"SetArgument\", sgenid" + (clist.getPos((arg1 & 0xFF), (arg1 >> 8 & 0xFF))) + ", " + ((arg1 >> 16) & 0xFFFF) + ");";
		case 37:
			return theIf + "Delay(" + arg1 * 35 / 1000 + ");";
		case 18:
			return theIf + "Thing_Remove(" + ((arg1 & 0xFF) << 5 | ((arg1 >> 8) & 0xFF)) + ");";
		case 7:
		{
			Thing thing = things.get(arg1 & 0xFF);
			return theIf + "SpawnForced(\"" + getThingName(thing.type, ((arg1 >> 8) & 0xFF)) + "\", getMediumX(" + thing.x + "), getMediumY(" + thing.y + "), 0);";
		}
		case 40:
			return theIf + "ScriptCall(\"NotebookAPI\", \"AddNotebookEntry\", getString(" + (arg1) + "));";
		}
		
		return "/* Not Implemented */";
	}
	
	public String toIR(int pos) {
		String cmdname = "UNKNOWN_" + cmdid;
		String cmdparams = "ARG1=" + arg1;
		String cmdopts = "( ";
		if ((arg2 & 0x100) != 0) {
			cmdopts += "USE ";
		}
		if ((arg2 & 0xF) != 0) {
			cmdopts += "ENTER_FROM( ";
			if ((arg2 & 0x8) != 0) {
				cmdopts += "WEST ";
			}
			if ((arg2 & 0x4) != 0) {
				cmdopts += "SOUTH ";
			}
			if ((arg2 & 0x2) != 0) {
				cmdopts += "EAST ";
			}
			if ((arg2 & 0x1) != 0) {
				cmdopts += "NORTH ";
			}
			cmdopts += ") ";
		}
		if ((arg2 & 0xF0) != 0) {
			cmdopts += "LEAVE_TO( ";
			if ((arg2 & 0x80) != 0) {
				cmdopts += "EAST ";
			}
			if ((arg2 & 0x40) != 0) {
				cmdopts += "NORTH ";
			}
			if ((arg2 & 0x20) != 0) {
				cmdopts += "WEST ";
			}
			if ((arg2 & 0x10) != 0) {
				cmdopts += "SOUTH ";
			}
			cmdopts += ") ";
		}
		if ((arg2 & 0x200) != 0) {
			cmdopts += "MODIFYWORLD ";
		}
		cmdopts += "IF_VAR( " + ((arg2 & 0xFFFF0000) >> 16) + " ) ";
		cmdopts += ")";

		switch (cmdid) {
		case 1:
			cmdname = "TELEPORT";
			cmdparams = "X=" + (arg1 & 0xFF) + " Y=" + ((arg1 >> 8) & 0xFF) + " DIR=" + ((arg1 >> 20) & 0x3FF); 
			break;
		case 2:
			cmdname = "CH_LEVEL";
			cmdparams = "LEVEL_NAME=" + (arg1 & 0xFF) + " UNKN=" + ((arg1 & 2147483647) >> 8) + " COMPLETE=" + (arg1 & -2147483648);
			break;
		case 3:
			cmdname = "RUNPOS";
			cmdparams = "X=" + (arg1 & 0xFF) + " Y=" + ((arg1 >> 8) & 0xFF) + " VAL=" + ((arg1 >> 16) & 0xFFFF); 
			break;
		case 4:
			cmdname = "STBARMSG";
			cmdparams = "STR=" + arg1;
			break;
		case 5:
			cmdname = "PLAYER_DAMAGE";
			cmdparams = "DMG=" + arg1;
			break;
		case 7:
			cmdname = "SHOW_THING";
			cmdparams = "ID=" + (arg1 & 0xFF) + " STATE=" + ((arg1 >> 8) & 0xFF); 
			break;
		case 8:
			cmdname = "SHOW_MONOLOG";
			cmdparams = "STR=" + (arg1);
			break;
		case 9:
			cmdname = "AUTOMAP";
			cmdparams = "";
			break;
		case 10:
			cmdname = "PASSCODE_OR_HALT";
			cmdparams = "PASS=" + (((arg1) & 0xFF)) + " MSG=" + (((arg1 >> 8) & 0xFF));
			break;
		case 11:
			cmdname = "SET_VAR";
			cmdparams = "X=" + (arg1 & 0xFF) + " Y=" + ((arg1 >> 8) & 0xFF) + " VAL=" + ((arg1 >> 16) & 0xFFFF); 
			break;
		case 12:
			cmdname = "DOOR_LOCK";
			cmdparams = "LINE=" + arg1;
			break;
		case 13:
			cmdname = "DOOR_UNLOCK";
			cmdparams = "LINE=" + arg1;
			break;
		case 15:
			cmdname = "DOOR_OPEN";
			cmdparams = "LINE=" + arg1;
			break;
		case 16:
			cmdname = "DOOR_CLOSE";
			cmdparams = "LINE=" + arg1;
			break;
		case 18:
			cmdname = "HIDE_THINGS_AT";
			cmdparams = "X=" + (arg1 & 0xFF) + " Y=" + ((arg1 >> 8) & 0xFF); 
			break;
		case 19:
			cmdname = "INC_VAR";
			cmdparams = "X=" + (arg1 & 0xFF) + " Y=" + ((arg1 >> 8) & 0xFF); 
			break;
		case 20:
			cmdname = "DEC_VAR";
			cmdparams = "X=" + (arg1 & 0xFF) + " Y=" + ((arg1 >> 8) & 0xFF); 
			break;
		case 21:
			cmdname = "INC_STAT";
			cmdparams = "AMOUNT=" + ((arg1 >> 8) & 0xFF) + " STAT=" + ((arg1) & 0xFF);
			break;
		case 22:
			cmdname = "DEC_STAT";
			cmdparams = "AMOUNT=" + ((arg1 >> 8) & 0xFF) + " STAT=" + ((arg1) & 0xFF);
			break;
		case 23:
			cmdname = "CHECK_STAT_OR_SHOW_MONOLOG";
			cmdparams = "STR=" + ((arg1 >> 16) & 0xFFFF) + " AMOUNT=" + ((arg1 >> 8) & 0xFF) + " STAT=" + ((arg1) & 0xFF);
			break;
		case 24:
			cmdname = "STBARMSG_ALT";
			cmdparams = "STR=" + arg1;
			break;
		case 25:
			cmdname = "EXPLODE";
			cmdparams = "X=" + (arg1 & 0xFF) + " Y=" + ((arg1 >> 8) & 0xFF) + " TYPE=" + ((arg1 >> 24) & 0xFF); 
			break;
		case 26:
			cmdname = "SHOW_MONOLOG_WITH_SOUND";
			cmdparams = "STR=" + arg1;
			break;
		case 27:
			cmdname = "NEXT_LEVEL_START_POS";
			cmdparams = "ARG1=" + arg1 + " X=" + ((arg1 >> 8) & 0xFF) + " Y=" + ((arg1 >> 16) & 0xFF);
			break;
		case 29:
			cmdname = "EARTHQUAKE";
			cmdparams = "TIME=" + (arg1 & 0xFFFFFF) + " INTENSITY=" +  ((arg1 >> 24) & 0xFF);
			break;
		case 30:
			cmdname = "FLOORCOLOR";
			cmdparams = "COLOR=" + (arg1 & 0xFFFFFF);
			break;
		case 31:
			cmdname = "CEILCOLOR";
			cmdparams = "COLOR=" + (arg1 & 0xFFFFFF);
			break;
		case 32:
			cmdname = "TAKE_OR_RETURN_WEAPONS";
			cmdparams = "ACTION=" + arg1;
			break;
		case 33:
			cmdname = "SHOP";
			cmdparams = "ID=" + arg1;
			break;
		case 34:
			cmdname = "SET_THING_STATE";
			cmdparams += " X=" + (arg1 & 0x1F) + " Y=" + ((arg1 >> 5) & 0x1F) + " VAL=" + ((arg1 >> 16) & 0xFF); 
			break;
		case 35:
			cmdname = "PARTICLE";
			cmdparams = "COLOR=" + (arg1 & 0xFFFFFF) + " EFFECT=" +  ((arg1 >> 24) & 0xFF);
			break;
		case 36:
			cmdname = "FRAME";
			break;
		case 37:
			cmdname = "SLEEP";
			cmdparams = "MSEC=" + arg1;
			break;
		case 38:
			cmdname = "UNKNOWN_REACTOR_LIFT";
			break;
		case 39:
			cmdname = "SHOW_MONOLOG_IF_LEVEL_UNFINISHED";
			cmdparams = "STR=" + (arg1 & 0xFFFF) + " LEVEL_ID=" +  ((arg1 >> 8) & 0xFFFF);
			break;
		case 40:
			cmdname = "NOTEBOOK_ADD";
			cmdparams = "STR=" + arg1;
			break;
		case 41:
			cmdname = "REQUIRE_KEYCARD";
			cmdparams = "KEY=" + arg1;
			break;

		default:
			break;
		}
		return cmdopts + " " + cmdname + " " + cmdparams;
	}
}
