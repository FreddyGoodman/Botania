/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.test;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;

import org.violetmoon.botania.test.block.ApothecaryRecipeTest;
import org.violetmoon.botania.test.block.BindingTest;
import org.violetmoon.botania.test.block.DandelifeonTest;
import org.violetmoon.botania.test.block.DrumBlockTest;
import org.violetmoon.botania.test.block.EntropinnyumUnethicalTntDetectionTest;
import org.violetmoon.botania.test.block.ForceRelayTest;
import org.violetmoon.botania.test.block.HopperhockTest;
import org.violetmoon.botania.test.block.IdleFlowerTickingTest;
import org.violetmoon.botania.test.block.InterceptorTest;
import org.violetmoon.botania.test.block.ManaSpreaderTest;
import org.violetmoon.botania.test.block.OpenCrateTest;
import org.violetmoon.botania.test.block.RannuncarpusTest;
import org.violetmoon.botania.test.block.TargetBlockTest;
import org.violetmoon.botania.test.item.AstrolabeTest;
import org.violetmoon.botania.test.item.CacophoniumTest;
import org.violetmoon.botania.test.item.FlowerPouchTest;
import org.violetmoon.botania.test.item.GrassSeedDispensingTest;
import org.violetmoon.botania.test.item.LifeAggregatorTest;
import org.violetmoon.botania.test.item.RarityConsistencyTest;
import org.violetmoon.botania.test.item.SpectatorScanTest;
import org.violetmoon.botania.test.item.lens.BoreLensTest;
import org.violetmoon.botania.test.item.lens.BoreWarpLensTest;
import org.violetmoon.botania.test.item.lens.EntropicWarpLensTest;
import org.violetmoon.botania.test.item.lens.PaintslingerLensTest;
import org.violetmoon.botania.test.item.lens.WarpForceTest;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class RegressionGameTests {
	private static final List<String> IDLE_FLOWERS = List.of(
			"agricarnation",
			"agricarnation_petite",
			"bellethorne",
			"bellethorne_petite",
			"bergamute",
			"bubbell",
			"bubbell_petite",
			"clayconia",
			"clayconia_petite",
			"dandelifeon",
			"dreadthorne",
			"entropinnyum",
			"exoflame",
			"fallen_kanade",
			"floating_agricarnation",
			"floating_agricarnation_petite",
			"floating_bellethorne",
			"floating_bellethorne_petite",
			"floating_bergamute",
			"floating_bubbell",
			"floating_bubbell_petite",
			"floating_clayconia",
			"floating_clayconia_petite",
			"floating_dandelifeon",
			"floating_dreadthorne",
			"floating_entropinnyum",
			"floating_exoflame",
			"floating_fallen_kanade",
			"floating_gourmaryllis",
			"floating_heisei_dream",
			"floating_hyacidus",
			"floating_jaded_amaranthus",
			"floating_jiyuulia",
			"floating_jiyuulia_petite",
			"floating_kekimurus",
			"floating_labellia",
			"floating_loonium",
			"floating_manastar",
			"floating_marimorphosis",
			"floating_marimorphosis_petite",
			"floating_medumone",
			"floating_narslimmus",
			"floating_orechid",
			"floating_orechid_ignem",
			"floating_pollidisiac",
			"floating_pure_daisy",
			"floating_rafflowsia",
			"floating_rosa_arcana",
			"floating_shulk_me_not",
			"floating_solegnolia",
			"floating_solegnolia_petite",
			"floating_spectranthemum",
			"floating_spectrolus",
			"floating_tangleberrie",
			"floating_tangleberrie_petite",
			"floating_tigerseye",
			"floating_vinculotus",
			"gourmaryllis",
			"heisei_dream",
			"hyacidus",
			"jaded_amaranthus",
			"jiyuulia",
			"jiyuulia_petite",
			"kekimurus",
			"labellia",
			"loonium",
			"manastar",
			"marimorphosis",
			"marimorphosis_petite",
			"medumone",
			"narslimmus",
			"orechid",
			"orechid_ignem",
			"pollidisiac",
			"pure_daisy",
			"rafflowsia",
			"rosa_arcana",
			"shulk_me_not",
			"solegnolia",
			"solegnolia_petite",
			"spectranthemum",
			"spectrolus",
			"tangleberrie",
			"tangleberrie_petite",
			"tigerseye",
			"vinculotus"
	);

	private RegressionGameTests() { }

	public static void register(BiConsumer<Consumer<GameTestHelper>, Identifier> registrar) {
		var translationKeysTest = new TranslationKeysTest();
		register(registrar, "translationkeystest/everyblockhasvaliddescriptionid", translationKeysTest::everyBlockHasValidDescriptionId);
		register(registrar, "translationkeystest/everyitemhasvaliddescriptionid", translationKeysTest::everyItemHasValidDescriptionId);
		register(registrar, "translationkeystest/everyblocktaghasatranslationkey", translationKeysTest::everyBlockTagHasATranslationKey);
		register(registrar, "translationkeystest/everyentitytaghasatranslationkey", translationKeysTest::everyEntityTagHasATranslationKey);
		register(registrar, "translationkeystest/everyfluidtaghasatranslationkey", translationKeysTest::everyFluidTagHasATranslationKey);
		register(registrar, "translationkeystest/everyitemtaghasatranslationkey", translationKeysTest::everyItemTagHasATranslationKey);
		register(registrar, "translationkeystest/everybiometaghasatranslationkey", translationKeysTest::everyBiomeTagHasATranslationKey);
		register(registrar, "translationkeystest/everystructurehasatranslationkey", translationKeysTest::everyStructureHasATranslationKey);

		var apothecaryRecipeTest = new ApothecaryRecipeTest();
		register(registrar, "apothecaryrecipetest/testitementerprevention", apothecaryRecipeTest::testItemEnterPrevention);

		var bindingTest = new BindingTest();
		register(registrar, "bindingtest/functionalflowerautobindtest", bindingTest::functionalFlowerAutoBindTest);
		register(registrar, "bindingtest/generatingflowerautobindtest", bindingTest::generatingFlowerAutoBindTest);
		register(registrar, "bindingtest/functionalflowermanualbindtest", bindingTest::functionalFlowerManualBindTest);
		register(registrar, "bindingtest/generatingflowermanualbindtest", bindingTest::generatingFlowerManualBindTest);
		register(registrar, "bindingtest/functionalflowerobediencestick", bindingTest::functionalFlowerObedienceStick);
		register(registrar, "bindingtest/generatingflowerobediencestick", bindingTest::generatingFlowerObedienceStick);

		var dandelifeonTest = new DandelifeonTest();
		register(registrar, "dandelifeontest/testdandelifeontransferring", dandelifeonTest::testDandelifeonTransferring);

		var drumBlockTest = new DrumBlockTest();
		register(registrar, "drumblocktest/testmilkinggoat", drumBlockTest::testMilkingGoat);
		register(registrar, "drumblocktest/testmilkingcow", drumBlockTest::testMilkingCow);
		register(registrar, "drumblocktest/testmilkingmooshroom", drumBlockTest::testMilkingMooshroom);
		register(registrar, "drumblocktest/testmilkingmooshroomsoup", drumBlockTest::testMilkingMooshroomSoup);
		register(registrar, "drumblocktest/testmilkingbrownmooshroomsuspiciousstew", drumBlockTest::testMilkingBrownMooshroomSuspiciousStew);
		register(registrar, "drumblocktest/testmilkingbabygoat", drumBlockTest::testMilkingBabyGoat);
		register(registrar, "drumblocktest/testmilkingbabymooshroom", drumBlockTest::testMilkingBabyMooshroom);
		register(registrar, "drumblocktest/testmilkingdeadanimal", drumBlockTest::testMilkingDeadAnimal);
		register(registrar, "drumblocktest/testmilkingnearbergamute", drumBlockTest::testMilkingNearBergamute);
		register(registrar, "drumblocktest/testshearingsheep", drumBlockTest::testShearingSheep);
		register(registrar, "drumblocktest/testshearingbabysheep", drumBlockTest::testShearingBabySheep);
		register(registrar, "drumblocktest/testshearingsnowgolem", drumBlockTest::testShearingSnowGolem);
		register(registrar, "drumblocktest/testshearingpumpkinlesssnowgolem", drumBlockTest::testShearingPumpkinlessSnowGolem);
		register(registrar, "drumblocktest/testshearingmooshroom", drumBlockTest::testShearingMooshroom);

		var entropinnyumUnethicalTntDetectionTest = new EntropinnyumUnethicalTntDetectionTest();
		register(registrar, "entropinnyumunethicaltntdetectiontest/testforcelensduper", entropinnyumUnethicalTntDetectionTest::testForceLensDuper);
		register(registrar, "entropinnyumunethicaltntdetectiontest/testforcerelayduper", entropinnyumUnethicalTntDetectionTest::testForceRelayDuper);
		register(registrar, "entropinnyumunethicaltntdetectiontest/testentitylessduper", entropinnyumUnethicalTntDetectionTest::testEntitylessDuper);
		register(registrar, "entropinnyumunethicaltntdetectiontest/testlegitimatetnt", entropinnyumUnethicalTntDetectionTest::testLegitimateTnt);

		var forceRelayTest = new ForceRelayTest();
		register(registrar, "forcerelaytest/testbasic", forceRelayTest::testBasic);
		register(registrar, "forcerelaytest/testimmovable", forceRelayTest::testImmovable);
		register(registrar, "forcerelaytest/teststickypistonmovenotpropagated", forceRelayTest::testStickyPistonMoveNotPropagated);
		register(registrar, "forcerelaytest/testmovinginpistonstructure", forceRelayTest::testMovingInPistonStructure);

		var hopperhockTest = new HopperhockTest();
		register(registrar, "hopperhocktest/testfurnaceinserttop", hopperhockTest::testFurnaceInsertTop);
		register(registrar, "hopperhocktest/testfurnaceinsertside", hopperhockTest::testFurnaceInsertSide);
		register(registrar, "hopperhocktest/testfurnaceinsertbottom", hopperhockTest::testFurnaceInsertBottom);
		register(registrar, "hopperhocktest/testfurnaceinventoryfull", hopperhockTest::testFurnaceInventoryFull);
		register(registrar, "hopperhocktest/testfurnaceaddtostack", hopperhockTest::testFurnaceAddToStack);
		register(registrar, "hopperhocktest/testfurnacewrongitemforslot", hopperhockTest::testFurnaceWrongItemForSlot);
		register(registrar, "hopperhocktest/testfurnacefiltermatch", hopperhockTest::testFurnaceFilterMatch);
		register(registrar, "hopperhocktest/testfurnacefilternomatch", hopperhockTest::testFurnaceFilterNoMatch);
		register(registrar, "hopperhocktest/testcomposterinserttop", hopperhockTest::testComposterInsertTop);
		register(registrar, "hopperhocktest/testcomposterinsertside", hopperhockTest::testComposterInsertSide);
		register(registrar, "hopperhocktest/testcomposterinsertbottom", hopperhockTest::testComposterInsertBottom);
		register(registrar, "hopperhocktest/testcomposterinsertwrongitem", hopperhockTest::testComposterInsertWrongItem);
		register(registrar, "hopperhocktest/testcomposterfull", hopperhockTest::testComposterFull);

		var interceptorTest = new InterceptorTest();
		register(registrar, "interceptortest/testapplerequest", interceptorTest::testAppleRequest);
		register(registrar, "interceptortest/testbootsrequest", interceptorTest::testBootsRequest);

		var manaSpreaderTest = new ManaSpreaderTest();
		register(registrar, "manaspreadertest/testspreaderdefaultaimhittingpool", manaSpreaderTest::testSpreaderDefaultAimHittingPool);

		var openCrateTest = new OpenCrateTest();
		register(registrar, "opencratetest/testbasic", openCrateTest::testBasic);
		register(registrar, "opencratetest/testblocked", openCrateTest::testBlocked);
		register(registrar, "opencratetest/testsnug", openCrateTest::testSnug);

		var rannuncarpusTest = new RannuncarpusTest();
		register(registrar, "rannuncarpustest/testdestinationfilterpositive", rannuncarpusTest::testDestinationFilterPositive);
		register(registrar, "rannuncarpustest/testdestinationfilternegative", rannuncarpusTest::testDestinationFilterNegative);
		register(registrar, "rannuncarpustest/testpickupfilterpositive", rannuncarpusTest::testPickupFilterPositive);
		register(registrar, "rannuncarpustest/testpickupfilternegative", rannuncarpusTest::testPickupFilterNegative);
		register(registrar, "rannuncarpustest/testmultipleplacements", rannuncarpusTest::testMultiplePlacements);
		register(registrar, "rannuncarpustest/testmultipleplacementsprefersemptyspots", rannuncarpusTest::testMultiplePlacementsPrefersEmptySpots);

		var targetBlockTest = new TargetBlockTest();
		register(registrar, "targetblocktest/testfakeburstnoshooty", targetBlockTest::testFakeBurstNoShooty);

		var astrolabeTest = new AstrolabeTest();
		register(registrar, "astrolabetest/testcandleshorizontal3x3", astrolabeTest::testCandlesHorizontal3x3);
		register(registrar, "astrolabetest/testcandleshorizontal3x3offhand", astrolabeTest::testCandlesHorizontal3x3Offhand);
		register(registrar, "astrolabetest/testcandleshorizontal3x3multiplesourcestalismanlast", astrolabeTest::testCandlesHorizontal3x3MultipleSourcesTalismanLast);
		register(registrar, "astrolabetest/testcandleshorizontal3x3multiplesourcestalismanfirst", astrolabeTest::testCandlesHorizontal3x3MultipleSourcesTalismanFirst);
		register(registrar, "astrolabetest/testcandlesvertical3x3", astrolabeTest::testCandlesVertical3x3);
		register(registrar, "astrolabetest/testcandlesvertical5x5", astrolabeTest::testCandlesVertical5x5);
		register(registrar, "astrolabetest/testdirectionallogsaxisx", astrolabeTest::testDirectionalLogsAxisX);
		register(registrar, "astrolabetest/testdirectionallogsaxisy", astrolabeTest::testDirectionalLogsAxisY);
		register(registrar, "astrolabetest/testdirectionallogsaxisz", astrolabeTest::testDirectionalLogsAxisZ);
		register(registrar, "astrolabetest/testdirectionalpistonaxisx", astrolabeTest::testDirectionalPistonAxisX);
		register(registrar, "astrolabetest/testdirectionalpistonaxisy", astrolabeTest::testDirectionalPistonAxisY);
		register(registrar, "astrolabetest/testdirectionalpistonaxisz", astrolabeTest::testDirectionalPistonAxisZ);
		register(registrar, "astrolabetest/testnotenoughmana", astrolabeTest::testNotEnoughMana);
		register(registrar, "astrolabetest/testbarelynotenoughmana", astrolabeTest::testBarelyNotEnoughMana);

		var cacophoniumTest = new CacophoniumTest();
		register(registrar, "cacophoniumtest/testcacophonium", cacophoniumTest::testCacophonium);

		var flowerPouchTest = new FlowerPouchTest();
		register(registrar, "flowerpouchtest/testnoshinyflowers", flowerPouchTest::testNoShinyFlowers);
		register(registrar, "flowerpouchtest/testpickupbasic", flowerPouchTest::testPickupBasic);

		var grassSeedDispensingTest = new GrassSeedDispensingTest();
		register(registrar, "grassseeddispensingtest/testgrassseeddispensing", grassSeedDispensingTest::TestGrassSeedDispensing);

		var lifeAggregatorTest = new LifeAggregatorTest();
		register(registrar, "lifeaggregatortest/testspawnermover", lifeAggregatorTest::testSpawnerMover);

		var rarityConsistencyTest = new RarityConsistencyTest();
		register(registrar, "rarityconsistencytest/checkrecipeoutputconsistency", rarityConsistencyTest::checkRecipeOutputConsistency);

		var spectatorScanTest = new SpectatorScanTest();
		register(registrar, "spectatorscantest/testspectatorscanmainhand", spectatorScanTest::testSpectatorScanMainHand);
		register(registrar, "spectatorscantest/testspectatorscanoffhand", spectatorScanTest::testSpectatorScanOffHand);
		register(registrar, "spectatorscantest/testspectatorscanbothhands", spectatorScanTest::testSpectatorScanBothHands);

		var boreLensTest = new BoreLensTest();
		register(registrar, "borelenstest/testsnow", boreLensTest::testSnow);
		register(registrar, "borelenstest/teststone", boreLensTest::testStone);
		register(registrar, "borelenstest/testlog", boreLensTest::testLog);
		register(registrar, "borelenstest/testironblocktoolow", boreLensTest::testIronBlockTooLow);
		register(registrar, "borelenstest/testironblock", boreLensTest::testIronBlock);
		register(registrar, "borelenstest/testgoldoretoolow", boreLensTest::testGoldOreTooLow);
		register(registrar, "borelenstest/testgoldore", boreLensTest::testGoldOre);
		register(registrar, "borelenstest/testobsidiantoolow", boreLensTest::testObsidianTooLow);
		register(registrar, "borelenstest/testobsidian", boreLensTest::testObsidian);

		var boreWarpLensTest = new BoreWarpLensTest();
		register(registrar, "borewarplenstest/testwarpborelens", boreWarpLensTest::testWarpBoreLens);
		register(registrar, "borewarplenstest/testborewarplens", boreWarpLensTest::testBoreWarpLens);

		var entropicWarpLensTest = new EntropicWarpLensTest();
		register(registrar, "entropicwarplenstest/testwarp", entropicWarpLensTest::testWarp);

		var paintslingerLensTest = new PaintslingerLensTest();
		register(registrar, "paintslingerlenstest/testpainting", paintslingerLensTest::testPainting);

		var warpForceTest = new WarpForceTest();
		register(registrar, "warpforcetest/testwarpforcelens", warpForceTest::testWarpForceLens);
		register(registrar, "warpforcetest/testforcewarplens", warpForceTest::testForceWarpLens);

		for (String flower : IDLE_FLOWERS) {
			register(registrar, "idleflowertickingtest/test_" + flower + "_idle",
					IdleFlowerTickingTest.testSpecialFlowerIdle(botaniaRL(flower)));
		}
	}

	private static void register(BiConsumer<Consumer<GameTestHelper>, Identifier> registrar, String path, Consumer<GameTestHelper> test) {
		registrar.accept(test, botaniaRL("regression/" + path));
	}
}
