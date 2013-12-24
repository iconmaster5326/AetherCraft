package com.iconmaster.aec.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DefaultEnergyValuesConfig {
	public static void createDefaultEvConfigFile(File configFile) {
		StringBuilder sb = new StringBuilder();
		sb.append("tile.web=64" + System.getProperty("line.separator"));
		sb.append("tile.gravel=8" + System.getProperty("line.separator"));
		sb.append("tile.pumpkin=16" + System.getProperty("line.separator"));
		sb.append("tile.sapling.oak=48" + System.getProperty("line.separator"));
		sb.append("enchantment_21=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_20=1024" + System.getProperty("line.separator"));
		sb.append("tile.hellsand=1" + System.getProperty("line.separator"));
		sb.append("item.minecartHopper=2592"
				+ System.getProperty("line.separator"));
		sb.append("item.minecartTnt=1605"
				+ System.getProperty("line.separator"));
		sb.append("tile.lever=3" + System.getProperty("line.separator"));
		sb.append("tile.stoneMoss=16" + System.getProperty("line.separator"));
		sb.append("item.goldNugget=455" + System.getProperty("line.separator"));
		sb.append("item.potatoPoisonous=32"
				+ System.getProperty("line.separator"));
		sb.append("item.diode=55" + System.getProperty("line.separator"));
		sb.append("item.carrotGolden=3672"
				+ System.getProperty("line.separator"));
		sb.append("item.helmetDiamond=40960"
				+ System.getProperty("line.separator"));
		sb.append("item.book=48" + System.getProperty("line.separator"));
		sb.append("item.shovelGold=4100" + System.getProperty("line.separator"));
		sb.append("tile.leaves.spruce=1" + System.getProperty("line.separator"));
		sb.append("item.shovelDiamond=8196"
				+ System.getProperty("line.separator"));
		sb.append("tile.stonebricksmooth.default=1"
				+ System.getProperty("line.separator"));
		sb.append("tile.stonebrick=1" + System.getProperty("line.separator"));
		sb.append("item.skull.char=8192" + System.getProperty("line.separator"));
		sb.append("item.hatchetIron=772" + System.getProperty("line.separator"));
		sb.append("item.chickenCooked=64"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.orange=32"
				+ System.getProperty("line.separator"));
		sb.append("tile.brick=32" + System.getProperty("line.separator"));
		sb.append("item.brick=8" + System.getProperty("line.separator"));
		sb.append("enchantment_32=1024" + System.getProperty("line.separator"));
		sb.append("tile.flower=16" + System.getProperty("line.separator"));
		sb.append("tile.gra.energyManipulator=34848"
				+ System.getProperty("line.separator"));
		sb.append("item.cookie=48" + System.getProperty("line.separator"));
		sb.append("tile.stonebricksmooth.mossy=1"
				+ System.getProperty("line.separator"));
		sb.append("enchantment_35=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_34=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_33=1024" + System.getProperty("line.separator"));
		sb.append("item.redstone=16" + System.getProperty("line.separator"));
		sb.append("item.charcoal=32" + System.getProperty("line.separator"));
		sb.append("item.bootsCloth=256" + System.getProperty("line.separator"));
		sb.append("tile.log.jungle=16" + System.getProperty("line.separator"));
		sb.append("item.helmetCloth=320" + System.getProperty("line.separator"));
		sb.append("item.hatchetWood=16" + System.getProperty("line.separator"));
		sb.append("item.seeds_pumpkin=64"
				+ System.getProperty("line.separator"));
		sb.append("tile.weightedPlate_light=8192"
				+ System.getProperty("line.separator"));
		sb.append("item.chestplateIron=2048"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.pink=32"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.gray=32"
				+ System.getProperty("line.separator"));
		sb.append("tile.workbench=16" + System.getProperty("line.separator"));
		sb.append("tile.cloth.silver=8" + System.getProperty("line.separator"));
		sb.append("tile.button=4" + System.getProperty("line.separator"));
		sb.append("tile.trapdoor=12" + System.getProperty("line.separator"));
		sb.append("item.frame=80" + System.getProperty("line.separator"));
		sb.append("tile.deadbush=16" + System.getProperty("line.separator"));
		sb.append("tile.sandStone.chiseled=4"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.green=32"
				+ System.getProperty("line.separator"));
		sb.append("item.hatchetDiamond=24580"
				+ System.getProperty("line.separator"));
		sb.append("item.painting=22" + System.getProperty("line.separator"));
		sb.append("item.pickaxeGold=12292"
				+ System.getProperty("line.separator"));
		sb.append("item.bone=160" + System.getProperty("line.separator"));
		sb.append("tile.dropper=1335" + System.getProperty("line.separator"));
		sb.append("tile.enchantmentTable=16688"
				+ System.getProperty("line.separator"));
		sb.append("tile.redstoneLight=640"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.brown=32"
				+ System.getProperty("line.separator"));
		sb.append("tile.cloth.brown=8" + System.getProperty("line.separator"));
		sb.append("item.shovelStone=5" + System.getProperty("line.separator"));
		sb.append("tile.stairsBrick=48" + System.getProperty("line.separator"));
		sb.append("item.minecartFurnace=1288"
				+ System.getProperty("line.separator"));
		sb.append("item.flint=8" + System.getProperty("line.separator"));
		sb.append("item.potato=32" + System.getProperty("line.separator"));
		sb.append("item.bucket=256" + System.getProperty("line.separator"));
		sb.append("tile.obsidian=64" + System.getProperty("line.separator"));
		sb.append("item.chickenRaw=64" + System.getProperty("line.separator"));
		sb.append("tile.pressurePlate=8" + System.getProperty("line.separator"));
		sb.append("tile.fenceIron=96" + System.getProperty("line.separator"));
		sb.append("enchantment_18=1024" + System.getProperty("line.separator"));
		sb.append("tile.stairsStone=1" + System.getProperty("line.separator"));
		sb.append("item.hoeIron=516" + System.getProperty("line.separator"));
		sb.append("enchantment_17=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_16=1024" + System.getProperty("line.separator"));
		sb.append("item.shovelIron=260" + System.getProperty("line.separator"));
		sb.append("item.clay=8" + System.getProperty("line.separator"));
		sb.append("tile.quartzBlock.chiseled=128"
				+ System.getProperty("line.separator"));
		sb.append("enchantment_19=1024" + System.getProperty("line.separator"));
		sb.append("tile.goldenRail=4099" + System.getProperty("line.separator"));
		sb.append("tile.cloth.red=8" + System.getProperty("line.separator"));
		sb.append("tile.pistonStickyBase=352"
				+ System.getProperty("line.separator"));
		sb.append("tile.dirt=1" + System.getProperty("line.separator"));
		sb.append("tile.cloth.orange=8" + System.getProperty("line.separator"));
		sb.append("item.swordWood=10" + System.getProperty("line.separator"));
		sb.append("item.hoeWood=12" + System.getProperty("line.separator"));
		sb.append("item.netherbrick=1" + System.getProperty("line.separator"));
		sb.append("tile.sapling.jungle=48"
				+ System.getProperty("line.separator"));
		sb.append("tile.weightedPlate_heavy=512"
				+ System.getProperty("line.separator"));
		sb.append("tile.cobbleWall.mossy=16"
				+ System.getProperty("line.separator"));
		sb.append("item.reeds=16" + System.getProperty("line.separator"));
		sb.append("item.blazePowder=64" + System.getProperty("line.separator"));
		sb.append("item.glassBottle=3" + System.getProperty("line.separator"));
		sb.append("tile.notGate=18" + System.getProperty("line.separator"));
		sb.append("item.compass=1040" + System.getProperty("line.separator"));
		sb.append("item.string=64" + System.getProperty("line.separator"));
		sb.append("item.spiderEye=64" + System.getProperty("line.separator"));
		sb.append("tile.mushroom=32" + System.getProperty("line.separator"));
		sb.append("item.swordStone=4" + System.getProperty("line.separator"));
		sb.append("tile.chestTrap=163" + System.getProperty("line.separator"));
		sb.append("item.yellowDust=128" + System.getProperty("line.separator"));
		sb.append("tile.fenceGate=16" + System.getProperty("line.separator"));
		sb.append("tile.netherFence=12" + System.getProperty("line.separator"));
		sb.append("tile.daylightDetector=105"
				+ System.getProperty("line.separator"));
		sb.append("item.leggingsCloth=448"
				+ System.getProperty("line.separator"));
		sb.append("tile.wood.oak=4" + System.getProperty("line.separator"));
		sb.append("tile.clay=32" + System.getProperty("line.separator"));
		sb.append("item.swordDiamond=16386"
				+ System.getProperty("line.separator"));
		sb.append("item.mushroomStew=44" + System.getProperty("line.separator"));
		sb.append("item.dyePowder.red=32"
				+ System.getProperty("line.separator"));
		sb.append("item.fermentedSpiderEye=96"
				+ System.getProperty("line.separator"));
		sb.append("tile.anvil.intact=7936"
				+ System.getProperty("line.separator"));
		sb.append("item.helmetChain=360" + System.getProperty("line.separator"));
		sb.append("item.dyePowder.cyan=32"
				+ System.getProperty("line.separator"));
		sb.append("item.sugar=16" + System.getProperty("line.separator"));
		sb.append("tile.sandStone.smooth=4"
				+ System.getProperty("line.separator"));
		sb.append("tile.fence=6" + System.getProperty("line.separator"));
		sb.append("tile.cloth.blue=8" + System.getProperty("line.separator"));
		sb.append("tile.tallgrass.fern=16"
				+ System.getProperty("line.separator"));
		sb.append("tile.beacon=32965" + System.getProperty("line.separator"));
		sb.append("item.dyePowder.black=32"
				+ System.getProperty("line.separator"));
		sb.append("item.minecartChest=1312"
				+ System.getProperty("line.separator"));
		sb.append("tile.pistonBase=288" + System.getProperty("line.separator"));
		sb.append("item.leggingsIron=1792"
				+ System.getProperty("line.separator"));
		sb.append("item.writingBook=128" + System.getProperty("line.separator"));
		sb.append("item.leather=64" + System.getProperty("line.separator"));
		sb.append("item.pumpkinPie=96" + System.getProperty("line.separator"));
		sb.append("item.pickaxeDiamond=24580"
				+ System.getProperty("line.separator"));
		sb.append("item.swordIron=514" + System.getProperty("line.separator"));
		sb.append("item.comparator=89" + System.getProperty("line.separator"));
		sb.append("enchantment_48=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_49=1024" + System.getProperty("line.separator"));
		sb.append("item.bread=48" + System.getProperty("line.separator"));
		sb.append("item.skull.creeper=8192"
				+ System.getProperty("line.separator"));
		sb.append("tile.grass=1" + System.getProperty("line.separator"));
		sb.append("tile.woodSlab.jungle=2"
				+ System.getProperty("line.separator"));
		sb.append("item.rottenFlesh=64" + System.getProperty("line.separator"));
		sb.append("item.dyePowder.yellow=32"
				+ System.getProperty("line.separator"));
		sb.append("item.feather=64" + System.getProperty("line.separator"));
		sb.append("tile.gra.energyContainer=66176"
				+ System.getProperty("line.separator"));
		sb.append("item.doorIron=1536" + System.getProperty("line.separator"));
		sb.append("tile.log.spruce=16" + System.getProperty("line.separator"));
		sb.append("tile.wood.jungle=4" + System.getProperty("line.separator"));
		sb.append("item.seeds=8" + System.getProperty("line.separator"));
		sb.append("item.carrotOnAStick=166"
				+ System.getProperty("line.separator"));
		sb.append("tile.woodSlab.birch=2"
				+ System.getProperty("line.separator"));
		sb.append("tile.quartzBlock.default=128"
				+ System.getProperty("line.separator"));
		sb.append("tile.blockIron=2304" + System.getProperty("line.separator"));
		sb.append("item.carrots=32" + System.getProperty("line.separator"));
		sb.append("item.chestplateCloth=512"
				+ System.getProperty("line.separator"));
		sb.append("item.blazeRod=320" + System.getProperty("line.separator"));
		sb.append("tile.blockGold=36864" + System.getProperty("line.separator"));
		sb.append("item.swordGold=8194" + System.getProperty("line.separator"));
		sb.append("tile.furnace=8" + System.getProperty("line.separator"));
		sb.append("item.skull.zombie=8192"
				+ System.getProperty("line.separator"));
		sb.append("enchantment_51=1024" + System.getProperty("line.separator"));
		sb.append("item.slimeball=64" + System.getProperty("line.separator"));
		sb.append("enchantment_50=1024" + System.getProperty("line.separator"));
		sb.append("item.leggingsChain=480"
				+ System.getProperty("line.separator"));
		sb.append("item.hatchetGold=12292"
				+ System.getProperty("line.separator"));
		sb.append("item.diamond=8192" + System.getProperty("line.separator"));
		sb.append("item.skull.skeleton=8192"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.magenta=32"
				+ System.getProperty("line.separator"));
		sb.append("item.chestplateGold=32768"
				+ System.getProperty("line.separator"));
		sb.append("tile.vine=1" + System.getProperty("line.separator"));
		sb.append("item.pickaxeWood=16" + System.getProperty("line.separator"));
		sb.append("tile.cloth.black=8" + System.getProperty("line.separator"));
		sb.append("tile.stairsWoodBirch=6"
				+ System.getProperty("line.separator"));
		sb.append("tile.sponge=1" + System.getProperty("line.separator"));
		sb.append("item.ingotIron=256" + System.getProperty("line.separator"));
		sb.append("item.map=1168" + System.getProperty("line.separator"));
		sb.append("item.ingotGold=4096" + System.getProperty("line.separator"));
		sb.append("tile.woodSlab.oak=2" + System.getProperty("line.separator"));
		sb.append("item.saddle=256" + System.getProperty("line.separator"));
		sb.append("item.shears=512" + System.getProperty("line.separator"));
		sb.append("item.coal=128" + System.getProperty("line.separator"));
		sb.append("tile.cloth.purple=8" + System.getProperty("line.separator"));
		sb.append("item.dyePowder.purple=32"
				+ System.getProperty("line.separator"));
		sb.append("item.melon=16" + System.getProperty("line.separator"));
		sb.append("tile.chest=32" + System.getProperty("line.separator"));
		sb.append("tile.melon=576" + System.getProperty("line.separator"));
		sb.append("tile.ladder=4" + System.getProperty("line.separator"));
		sb.append("item.pickaxeStone=7" + System.getProperty("line.separator"));
		sb.append("tile.cloth.magenta=8" + System.getProperty("line.separator"));
		sb.append("tile.mycel=1" + System.getProperty("line.separator"));
		sb.append("tile.stonebricksmooth.chiseled=1"
				+ System.getProperty("line.separator"));
		sb.append("tile.woodSlab.spruce=2"
				+ System.getProperty("line.separator"));
		sb.append("item.cauldron=1792" + System.getProperty("line.separator"));
		sb.append("item.dyePowder.lightBlue=32"
				+ System.getProperty("line.separator"));
		sb.append("item.wheat=16" + System.getProperty("line.separator"));
		sb.append("item.bootsIron=1024" + System.getProperty("line.separator"));
		sb.append("tile.hellrock=1" + System.getProperty("line.separator"));
		sb.append("item.paper=16" + System.getProperty("line.separator"));
		sb.append("tile.tallgrass.grass=16"
				+ System.getProperty("line.separator"));
		sb.append("item.snowball=1" + System.getProperty("line.separator"));
		sb.append("item.bootsDiamond=32768"
				+ System.getProperty("line.separator"));
		sb.append("item.shovelWood=8" + System.getProperty("line.separator"));
		sb.append("item.sign=26" + System.getProperty("line.separator"));
		sb.append("tile.netherStalk=128" + System.getProperty("line.separator"));
		sb.append("tile.blockRedstone=144"
				+ System.getProperty("line.separator"));
		sb.append("tile.leaves.jungle=1" + System.getProperty("line.separator"));
		sb.append("item.bow=198" + System.getProperty("line.separator"));
		sb.append("item.bowl=12" + System.getProperty("line.separator"));
		sb.append("item.magmaCream=128" + System.getProperty("line.separator"));
		sb.append("item.bootsChain=300" + System.getProperty("line.separator"));
		sb.append("item.beefRaw=64" + System.getProperty("line.separator"));
		sb.append("tile.stairsWoodSpruce=6"
				+ System.getProperty("line.separator"));
		sb.append("item.hatchetStone=7" + System.getProperty("line.separator"));
		sb.append("tile.blockLapis=1152" + System.getProperty("line.separator"));
		sb.append("item.egg=64" + System.getProperty("line.separator"));
		sb.append("item.record=8192" + System.getProperty("line.separator"));
		sb.append("tile.stairsNetherBrick=6"
				+ System.getProperty("line.separator"));
		sb.append("item.leggingsGold=28672"
				+ System.getProperty("line.separator"));
		sb.append("tile.cloth.pink=8" + System.getProperty("line.separator"));
		sb.append("tile.stone=1" + System.getProperty("line.separator"));
		sb.append("item.fireball=160" + System.getProperty("line.separator"));
		sb.append("item.porkchopRaw=64" + System.getProperty("line.separator"));
		sb.append("tile.lightgem=512" + System.getProperty("line.separator"));
		sb.append("item.fishingRod=134" + System.getProperty("line.separator"));
		sb.append("tile.cobbleWall.normal=1"
				+ System.getProperty("line.separator"));
		sb.append("tile.stairsWoodJungle=6"
				+ System.getProperty("line.separator"));
		sb.append("tile.cloth.yellow=8" + System.getProperty("line.separator"));
		sb.append("item.pickaxeIron=772" + System.getProperty("line.separator"));
		sb.append("item.hoeGold=8196" + System.getProperty("line.separator"));
		sb.append("item.emerald=16384" + System.getProperty("line.separator"));
		sb.append("item.arrow=74" + System.getProperty("line.separator"));
		sb.append("item.helmetIron=1280" + System.getProperty("line.separator"));
		sb.append("tile.sand=1" + System.getProperty("line.separator"));
		sb.append("item.seeds_melon=64" + System.getProperty("line.separator"));
		sb.append("tile.sandStone.default=4"
				+ System.getProperty("line.separator"));
		sb.append("tile.wood.spruce=4" + System.getProperty("line.separator"));
		sb.append("item.sulphur=64" + System.getProperty("line.separator"));
		sb.append("item.leggingsDiamond=57344"
				+ System.getProperty("line.separator"));
		sb.append("tile.cloth.green=8" + System.getProperty("line.separator"));
		sb.append("tile.sapling.spruce=48"
				+ System.getProperty("line.separator"));
		sb.append("item.flowerPot=24" + System.getProperty("line.separator"));
		sb.append("item.cake=2472" + System.getProperty("line.separator"));
		sb.append("tile.log.oak=16" + System.getProperty("line.separator"));
		sb.append("tile.cloth.lime=8" + System.getProperty("line.separator"));
		sb.append("tile.activatorRail=1558"
				+ System.getProperty("line.separator"));
		sb.append("item.doorWood=24" + System.getProperty("line.separator"));
		sb.append("tile.dragonEgg=1024000"
				+ System.getProperty("line.separator"));
		sb.append("item.enderPearl=64" + System.getProperty("line.separator"));
		sb.append("item.dyePowder.blue=128"
				+ System.getProperty("line.separator"));
		sb.append("tile.dispenser=221" + System.getProperty("line.separator"));
		sb.append("tile.stonebricksmooth.cracked=1"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.lime=32"
				+ System.getProperty("line.separator"));
		sb.append("item.potatoBaked=32" + System.getProperty("line.separator"));
		sb.append("tile.rail=96" + System.getProperty("line.separator"));
		sb.append("item.hoeDiamond=16388"
				+ System.getProperty("line.separator"));
		sb.append("item.beefCooked=64" + System.getProperty("line.separator"));
		sb.append("item.brewingStand=131"
				+ System.getProperty("line.separator"));
		sb.append("item.netherStalkSeeds=64"
				+ System.getProperty("line.separator"));
		sb.append("tile.jukebox=8224" + System.getProperty("line.separator"));
		sb.append("tile.leaves.oak=1" + System.getProperty("line.separator"));
		sb.append("tile.tallgrass.shrub=16"
				+ System.getProperty("line.separator"));
		sb.append("item.chestplateDiamond=65536"
				+ System.getProperty("line.separator"));
		sb.append("tile.stairsSandStone=6"
				+ System.getProperty("line.separator"));
		sb.append("item.bootsGold=16384" + System.getProperty("line.separator"));
		sb.append("item.appleGold=294976"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.silver=32"
				+ System.getProperty("line.separator"));
		sb.append("tile.cloth.white=8" + System.getProperty("line.separator"));
		sb.append("tile.stairsQuartz=192"
				+ System.getProperty("line.separator"));
		sb.append("item.flintAndSteel=264"
				+ System.getProperty("line.separator"));
		sb.append("item.apple=64" + System.getProperty("line.separator"));
		sb.append("tile.blockDiamond=73728"
				+ System.getProperty("line.separator"));
		sb.append("item.netherStar=32768"
				+ System.getProperty("line.separator"));
		sb.append("item.ghastTear=64" + System.getProperty("line.separator"));
		sb.append("tile.netherBrick=4" + System.getProperty("line.separator"));
		sb.append("tile.bookshelf=168" + System.getProperty("line.separator"));
		sb.append("tile.sapling.birch=48"
				+ System.getProperty("line.separator"));
		sb.append("tile.tnt=325" + System.getProperty("line.separator"));
		sb.append("tile.cloth.cyan=8" + System.getProperty("line.separator"));
		sb.append("tile.blockEmerald=147456"
				+ System.getProperty("line.separator"));
		sb.append("item.eyeOfEnder=128" + System.getProperty("line.separator"));
		sb.append("tile.cactus=32" + System.getProperty("line.separator"));
		sb.append("tile.stairsStoneBrickSmooth=1"
				+ System.getProperty("line.separator"));
		sb.append("tile.leaves.birch=1" + System.getProperty("line.separator"));
		sb.append("tile.quartzBlock.lines=128"
				+ System.getProperty("line.separator"));
		sb.append("enchantment_5=1024" + System.getProperty("line.separator"));
		sb.append("item.boat=20" + System.getProperty("line.separator"));
		sb.append("enchantment_4=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_7=1024" + System.getProperty("line.separator"));
		sb.append("item.bucketWater=257" + System.getProperty("line.separator"));
		sb.append("enchantment_6=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_1=1024" + System.getProperty("line.separator"));
		sb.append("enchantment_0=1024" + System.getProperty("line.separator"));
		sb.append("tile.whiteStone=1" + System.getProperty("line.separator"));
		sb.append("enchantment_3=1024" + System.getProperty("line.separator"));
		sb.append("item.fishCooked=64" + System.getProperty("line.separator"));
		sb.append("enchantment_2=1024" + System.getProperty("line.separator"));
		sb.append("item.milk=776" + System.getProperty("line.separator"));
		sb.append("tile.waterlily=1" + System.getProperty("line.separator"));
		sb.append("tile.cloth.lightBlue=8"
				+ System.getProperty("line.separator"));
		sb.append("item.speckledMelon=471"
				+ System.getProperty("line.separator"));
		sb.append("item.stick=2" + System.getProperty("line.separator"));
		sb.append("item.bucketLava=272" + System.getProperty("line.separator"));
		sb.append("tile.glass=1" + System.getProperty("line.separator"));
		sb.append("tile.litpumpkin=24" + System.getProperty("line.separator"));
		sb.append("item.porkchopCooked=64"
				+ System.getProperty("line.separator"));
		sb.append("tile.hopper=1312" + System.getProperty("line.separator"));
		sb.append("item.minecart=1280" + System.getProperty("line.separator"));
		sb.append("tile.log.birch=16" + System.getProperty("line.separator"));
		sb.append("tile.tripWireSource=131"
				+ System.getProperty("line.separator"));
		sb.append("tile.wood.birch=4" + System.getProperty("line.separator"));
		sb.append("item.helmetGold=20480"
				+ System.getProperty("line.separator"));
		sb.append("tile.cloth.gray=8" + System.getProperty("line.separator"));
		sb.append("item.chestplateChain=540"
				+ System.getProperty("line.separator"));
		sb.append("tile.rose=16" + System.getProperty("line.separator"));
		sb.append("item.skull.wither=8192"
				+ System.getProperty("line.separator"));
		sb.append("item.dyePowder.white=32"
				+ System.getProperty("line.separator"));
		sb.append("tile.detectorRail=259"
				+ System.getProperty("line.separator"));
		sb.append("item.clock=16400" + System.getProperty("line.separator"));
		sb.append("item.fishRaw=64" + System.getProperty("line.separator"));
		sb.append("item.netherquartz=32" + System.getProperty("line.separator"));
		sb.append("item.bed=36" + System.getProperty("line.separator"));
		sb.append("tile.stairsWood=6" + System.getProperty("line.separator"));
		sb.append("tile.musicBlock=48" + System.getProperty("line.separator"));
		sb.append("tile.torch=8" + System.getProperty("line.separator"));
		sb.append("tile.snow=4" + System.getProperty("line.separator"));
		sb.append("tile.enderChest=640" + System.getProperty("line.separator"));
		sb.append("tile.ice=1" + System.getProperty("line.separator"));

		try {
			PrintWriter pw = new PrintWriter(configFile);
			pw.print(sb.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
