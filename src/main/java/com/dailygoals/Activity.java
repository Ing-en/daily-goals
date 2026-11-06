package com.dailygoals;

import java.util.Arrays;
import java.util.Locale;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.gameval.SpriteID;
import net.runelite.client.hiscore.HiscoreSkill;

/**
 * Bosses and other activities a count goal can track. Bosses are matched against the name in
 * the game's "Your X kill count is: N" message, with aliases for messages that word the name
 * differently. The other activities have their own messages; see {@link Kind}.
 * <p>
 * Constant names are stored in saved goals, so don't rename them.
 * <p>
 * Names and icons come from the client's {@link HiscoreSkill} list, looked up by name while the
 * plugin runs rather than referenced in code. RuneLite renames entries now and then (e.g.
 * FRACTURED_ARCHIVE became THE_FRACTURED_ARCHIVE), and a missing one should only cost an icon,
 * not stop the plugin from building.
 */
enum Activity
{
	ABYSSAL_SIRE,
	ALCHEMICAL_HYDRA,
	AMOXLIATL,
	ARAXXOR,
	ARTIO,
	BARROWS_CHESTS("Barrows chest"),
	BRUTUS,
	BRYOPHYTA,
	CALLISTO,
	CALVARION,
	CERBERUS,
	CHAMBERS_OF_XERIC,
	CHAMBERS_OF_XERIC_CHALLENGE_MODE,
	CHAOS_ELEMENTAL,
	CHAOS_FANATIC,
	COMMANDER_ZILYANA,
	CORPOREAL_BEAST,
	CRAZY_ARCHAEOLOGIST,
	DAGANNOTH_PRIME,
	DAGANNOTH_REX,
	DAGANNOTH_SUPREME,
	DERANGED_ARCHAEOLOGIST,
	DOOM_OF_MOKHAIOTL,
	DUKE_SUCELLUS,
	FRACTURED_ARCHIVE,
	GENERAL_GRAARDOR,
	GIANT_MOLE,
	GROTESQUE_GUARDIANS,
	HESPORI,
	KALPHITE_QUEEN,
	KING_BLACK_DRAGON,
	KRAKEN,
	KREEARRA,
	KRIL_TSUTSAROTH,
	LUNAR_CHESTS("Lunar Chest"),
	MAD_ANGEL,
	MAGGOT_KING,
	MIMIC,
	NEX,
	NIGHTMARE,
	PHOSANIS_NIGHTMARE,
	OBOR,
	PHANTOM_MUSPAH,
	SARACHNIS,
	SCORPIA,
	SCURRIUS,
	SHELLBANE_GRYPHON,
	SKOTIZO,
	SOL_HEREDIT,
	SPINDEL,
	TEMPOROSS,
	THE_GAUNTLET,
	THE_CORRUPTED_GAUNTLET,
	THE_HUEYCOATL,
	THE_LEVIATHAN,
	THE_ROYAL_TITANS("Royal Titan"),
	THE_WHISPERER,
	THEATRE_OF_BLOOD,
	THEATRE_OF_BLOOD_HARD_MODE,
	THERMONUCLEAR_SMOKE_DEVIL,
	TOMBS_OF_AMASCUT,
	TOMBS_OF_AMASCUT_EXPERT,
	TZKAL_ZUK,
	TZTOK_JAD,
	VARDORVIS,
	VENENATIS,
	VETION,
	VORKATH,
	WINTERTODT,
	YAMA,
	ZALCANO,
	ZULRAH,

	CLUES_ALL("Clue scrolls (all)", Kind.CLUE_ANY, hiscoreSprite("CLUE_SCROLL_ALL"), "Clue scrolls"),
	CLUES_BEGINNER("Clue scrolls (beginner)", Kind.CLUE, hiscoreSprite("CLUE_SCROLL_ALL"), "Beginner clues"),
	CLUES_EASY("Clue scrolls (easy)", Kind.CLUE, hiscoreSprite("CLUE_SCROLL_ALL"), "Easy clues"),
	CLUES_MEDIUM("Clue scrolls (medium)", Kind.CLUE, hiscoreSprite("CLUE_SCROLL_ALL"), "Medium clues"),
	CLUES_HARD("Clue scrolls (hard)", Kind.CLUE, hiscoreSprite("CLUE_SCROLL_ALL"), "Hard clues"),
	CLUES_ELITE("Clue scrolls (elite)", Kind.CLUE, hiscoreSprite("CLUE_SCROLL_ALL"), "Elite clues"),
	CLUES_MASTER("Clue scrolls (master)", Kind.CLUE, hiscoreSprite("CLUE_SCROLL_ALL"), "Master clues"),
	RIFTS_CLOSED("Rifts closed (GotR)", Kind.RIFTS, hiscoreSprite("RIFTS_CLOSED"), "Rifts closed"),
	SEPULCHRE_FLOORS("Hallowed Sepulchre floors", Kind.SEPULCHRE, hiscoreSprite("AGILITY"), "Sepulchre floors"),

	OTHER("Other", Kind.KILL, SpriteID.SideIcons.COMBAT, null);

	/**
	 * Which game message counts toward it. See {@link CountMessage} for the patterns.
	 */
	enum Kind
	{
		/** "Your X kill count is: N." */
		KILL,
		/** "You have completed N medium Treasure Trails." for one tier. */
		CLUE,
		/** The same message for any tier; each one counts as one. */
		CLUE_ANY,
		/** "Amount of Rifts you have closed: N." */
		RIFTS,
		/** "You have completed Floor F of the Hallowed Sepulchre! Total completions: N." */
		SEPULCHRE
	}

	/**
	 * The heading the goal picker lists it under.
	 */
	@RequiredArgsConstructor
	enum Category
	{
		BOSS("Bosses"),
		RAID("Raids"),
		MINIGAME("Minigames & activities"),
		CLUE("Clue scrolls"),
		OTHER("Other");

		@Getter
		private final String label;
	}

	@Getter
	private final String name;
	@Getter
	private final Kind kind;
	@Getter
	private final int spriteId;
	/** What a goal on a non-boss activity counts, e.g. "Medium clues"; null for bosses. */
	@Nullable
	private final String countName;
	/** Normalized names a kill count message may use for this boss. */
	private final String[] keys;

	/**
	 * A boss, named after its hiscores entry.
	 */
	Activity(String... aliases)
	{
		HiscoreSkill hiscore = findHiscore(name());
		this.name = hiscore != null ? hiscore.getName() : titleCase(name());
		this.kind = Kind.KILL;
		this.spriteId = hiscore != null ? hiscore.getSpriteId() : SpriteID.SideIcons.COMBAT;
		this.countName = null;
		this.keys = new String[aliases.length + 1];
		for (int i = 0; i < aliases.length; i++)
		{
			keys[i] = normalize(aliases[i]);
		}
		keys[aliases.length] = normalize(this.name);
	}

	Activity(String name, Kind kind, int spriteId, @Nullable String countName)
	{
		this.name = name;
		this.kind = kind;
		this.spriteId = spriteId;
		this.countName = countName;
		this.keys = new String[0];
	}

	/**
	 * Whether the boss name from a kill count message is this boss.
	 */
	boolean matches(String chatName)
	{
		return kind == Kind.KILL && Arrays.asList(keys).contains(normalize(chatName));
	}

	Category getCategory()
	{
		switch (this)
		{
			case CHAMBERS_OF_XERIC:
			case CHAMBERS_OF_XERIC_CHALLENGE_MODE:
			case THEATRE_OF_BLOOD:
			case THEATRE_OF_BLOOD_HARD_MODE:
			case TOMBS_OF_AMASCUT:
			case TOMBS_OF_AMASCUT_EXPERT:
				return Category.RAID;
			case BARROWS_CHESTS:
			case LUNAR_CHESTS:
			case THE_GAUNTLET:
			case THE_CORRUPTED_GAUNTLET:
			case TEMPOROSS:
			case WINTERTODT:
			case ZALCANO:
			case RIFTS_CLOSED:
			case SEPULCHRE_FLOORS:
				return Category.MINIGAME;
			case OTHER:
				return Category.OTHER;
			default:
				return kind == Kind.CLUE || kind == Kind.CLUE_ANY ? Category.CLUE : Category.BOSS;
		}
	}

	/**
	 * The clue tier word in the completion message ("medium"), for single-tier clue goals.
	 */
	@Nullable
	String clueTier()
	{
		return kind == Kind.CLUE && countName != null ? countName.split(" ")[0].toLowerCase(Locale.ROOT) : null;
	}

	/**
	 * What a goal on this counts, e.g. "Zulrah kills" or "Medium clues".
	 */
	String goalName(String customName)
	{
		if (countName != null)
		{
			return countName;
		}
		String boss = this == OTHER ? customName : name;
		return (boss.isEmpty() ? "Boss" : boss) + " kills";
	}

	/**
	 * The goal picker shows the full name; "Other" asks for a name.
	 */
	String pickerName()
	{
		return this == OTHER ? "Other (type a name)" : name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	/**
	 * The hiscores entry with this constant name, also allowing for a "THE_" prefix being added
	 * or dropped. Null if the client doesn't have it.
	 */
	@Nullable
	private static HiscoreSkill findHiscore(String constant)
	{
		for (HiscoreSkill skill : HiscoreSkill.values())
		{
			if (sameEntry(constant, skill.name()))
			{
				return skill;
			}
		}
		return null;
	}

	/**
	 * Whether two constant names are the same entry, ignoring a leading "THE_".
	 */
	static boolean sameEntry(String a, String b)
	{
		return withoutThe(a).equals(withoutThe(b));
	}

	private static String withoutThe(String constant)
	{
		return constant.startsWith("THE_") ? constant.substring(4) : constant;
	}

	/**
	 * The icon of a hiscores entry, or a generic one if the client doesn't have it.
	 */
	private static int hiscoreSprite(String constant)
	{
		HiscoreSkill skill = findHiscore(constant);
		return skill != null && skill.getSpriteId() != -1 ? skill.getSpriteId() : SpriteID.SideIcons.COMBAT;
	}

	/**
	 * "THE_ROYAL_TITANS" -> "The Royal Titans", for a boss the client's hiscores don't list.
	 */
	static String titleCase(String constant)
	{
		StringBuilder name = new StringBuilder();
		for (String word : constant.toLowerCase(Locale.ROOT).split("_"))
		{
			if (name.length() > 0)
			{
				name.append(' ');
			}
			name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
		}
		return name.toString();
	}

	/**
	 * Lower case, no leading "the", letters and digits only, so "Kree'arra" == "Kree'Arra" and
	 * "Gauntlet" == "The Gauntlet".
	 */
	static String normalize(String name)
	{
		String s = name.toLowerCase(Locale.ROOT).trim();
		if (s.startsWith("the "))
		{
			s = s.substring(4);
		}
		return s.replaceAll("[^a-z0-9]", "");
	}
}
