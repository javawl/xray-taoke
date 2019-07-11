package com.xray.taoke.admin.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil {
	private static Map<String, String> utf2sb = new HashMap<String, String>();
	private static Map<String, String> sb2utf = new HashMap<String, String>();

	static {
		utf2sb.put("1F604", "\uE415");
		utf2sb.put("1F60A", "\uE056");
		utf2sb.put("1F603", "\uE057");
		utf2sb.put("263A", "\uE414");
		utf2sb.put("1F609", "\uE405");
		utf2sb.put("1F60D", "\uE106");
		utf2sb.put("1F618", "\uE418");
		utf2sb.put("1F61A", "\uE417");
		utf2sb.put("1F633", "\uE40D");
		utf2sb.put("1F60C", "\uE40A");
		utf2sb.put("1F601", "\uE404");
		utf2sb.put("1F61C", "\uE105");
		utf2sb.put("1F61D", "\uE409");
		utf2sb.put("1F612", "\uE40E");
		utf2sb.put("1F60F", "\uE402");
		utf2sb.put("1F613", "\uE108");
		utf2sb.put("1F614", "\uE403");
		utf2sb.put("1F61E", "\uE058");
		utf2sb.put("1F616", "\uE407");
		utf2sb.put("1F625", "\uE401");
		utf2sb.put("1F630", "\uE40F");
		utf2sb.put("1F628", "\uE40B");
		utf2sb.put("1F623", "\uE406");
		utf2sb.put("1F622", "\uE413");
		utf2sb.put("1F62D", "\uE411");
		utf2sb.put("1F602", "\uE412");
		utf2sb.put("1F632", "\uE410");
		utf2sb.put("1F631", "\uE107");
		utf2sb.put("1F620", "\uE059");
		utf2sb.put("1F621", "\uE416");
		utf2sb.put("1F62A", "\uE408");
		utf2sb.put("1F637", "\uE40C");
		utf2sb.put("1F47F", "\uE11A");
		utf2sb.put("1F47D", "\uE10C");
		utf2sb.put("1F49B", "\uE32C");
		utf2sb.put("1F499", "\uE32A");
		utf2sb.put("1F49C", "\uE32D");
		utf2sb.put("1F497", "\uE328");
		utf2sb.put("1F49A", "\uE32B");
		utf2sb.put("2764", "\uE022");
		utf2sb.put("1F494", "\uE023");
		utf2sb.put("1F493", "\uE327");
		utf2sb.put("1F498", "\uE329");
		utf2sb.put("2728", "\uE32E");
		utf2sb.put("1F31F", "\uE335");
		utf2sb.put("1F4A2", "\uE334");
		utf2sb.put("2755", "\uE337");
		utf2sb.put("2754", "\uE336");
		utf2sb.put("1F4A4", "\uE13C");
		utf2sb.put("1F4A8", "\uE330");
		utf2sb.put("1F4A6", "\uE331");
		utf2sb.put("1F3B6", "\uE326");
		utf2sb.put("1F3B5", "\uE03E");
		utf2sb.put("1F525", "\uE11D");
		utf2sb.put("1F4A9", "\uE05A");
		utf2sb.put("1F44D", "\uE00E");
		utf2sb.put("1F44E", "\uE421");
		utf2sb.put("1F44C", "\uE420");
		utf2sb.put("1F44A", "\uE00D");
		utf2sb.put("270A", "\uE010");
		utf2sb.put("270C", "\uE011");
		utf2sb.put("1F44B", "\uE41E");
		utf2sb.put("270B", "\uE012");
		utf2sb.put("1F450", "\uE422");
		utf2sb.put("1F446", "\uE22E");
		utf2sb.put("1F447", "\uE22F");
		utf2sb.put("1F449", "\uE231");
		utf2sb.put("1F448", "\uE230");
		utf2sb.put("1F64C", "\uE427");
		utf2sb.put("1F64F", "\uE41D");
		utf2sb.put("261D", "\uE00F");
		utf2sb.put("1F44F", "\uE41F");
		utf2sb.put("1F4AA", "\uE14C");
		utf2sb.put("1F6B6", "\uE201");
		utf2sb.put("1F3C3", "\uE115");
		utf2sb.put("1F46B", "\uE428");
		utf2sb.put("1F483", "\uE51F");
		utf2sb.put("1F46F", "\uE429");
		utf2sb.put("1F646", "\uE424");
		utf2sb.put("1F645", "\uE423");
		utf2sb.put("1F481", "\uE253");
		utf2sb.put("1F647", "\uE426");
		utf2sb.put("1F48F", "\uE111");
		utf2sb.put("1F491", "\uE425");
		utf2sb.put("1F486", "\uE31E");
		utf2sb.put("1F487", "\uE31F");
		utf2sb.put("1F485", "\uE31D");
		utf2sb.put("1F466", "\uE001");
		utf2sb.put("1F467", "\uE002");
		utf2sb.put("1F469", "\uE005");
		utf2sb.put("1F468", "\uE004");
		utf2sb.put("1F476", "\uE51A");
		utf2sb.put("1F475", "\uE519");
		utf2sb.put("1F474", "\uE518");
		utf2sb.put("1F471", "\uE515");
		utf2sb.put("1F472", "\uE516");
		utf2sb.put("1F473", "\uE517");
		utf2sb.put("1F477", "\uE51B");
		utf2sb.put("1F46E", "\uE152");
		utf2sb.put("1F47C", "\uE04E");
		utf2sb.put("1F478", "\uE51C");
		utf2sb.put("1F482", "\uE51E");
		utf2sb.put("1F480", "\uE11C");
		utf2sb.put("1F463", "\uE536");
		utf2sb.put("1F48B", "\uE003");
		utf2sb.put("1F444", "\uE41C");
		utf2sb.put("1F442", "\uE41B");
		utf2sb.put("1F440", "\uE419");
		utf2sb.put("1F443", "\uE41A");
		utf2sb.put("1F38D", "\uE436");
		utf2sb.put("1F49D", "\uE437");
		utf2sb.put("1F38E", "\uE438");
		utf2sb.put("1F392", "\uE43A");
		utf2sb.put("1F393", "\uE439");
		utf2sb.put("1F38F", "\uE43B");
		utf2sb.put("1F386", "\uE117");
		utf2sb.put("1F387", "\uE440");
		utf2sb.put("1F390", "\uE442");
		utf2sb.put("1F391", "\uE446");
		utf2sb.put("1F383", "\uE445");
		utf2sb.put("1F47B", "\uE11B");
		utf2sb.put("1F385", "\uE448");
		utf2sb.put("1F384", "\uE033");
		utf2sb.put("1F381", "\uE112");
		utf2sb.put("1F514", "\uE325");
		utf2sb.put("1F389", "\uE312");
		utf2sb.put("1F388", "\uE310");
		utf2sb.put("1F4BF", "\uE126");
		utf2sb.put("1F4C0", "\uE127");
		utf2sb.put("1F4F7", "\uE008");
		utf2sb.put("1F3A5", "\uE03D");
		utf2sb.put("1F4BB", "\uE00C");
		utf2sb.put("1F4FA", "\uE12A");
		utf2sb.put("1F4F1", "\uE00A");
		utf2sb.put("1F4E0", "\uE00B");
		utf2sb.put("260E", "\uE009");
		utf2sb.put("1F4BD", "\uE316");
		utf2sb.put("1F4FC", "\uE129");
		utf2sb.put("1F50A", "\uE141");
		utf2sb.put("1F4E2", "\uE142");
		utf2sb.put("1F4E3", "\uE317");
		utf2sb.put("1F4FB", "\uE128");
		utf2sb.put("1F4E1", "\uE14B");
		utf2sb.put("27BF", "\uE211");
		utf2sb.put("1F50D", "\uE114");
		utf2sb.put("1F513", "\uE145");
		utf2sb.put("1F512", "\uE144");
		utf2sb.put("1F511", "\uE03F");
		utf2sb.put("2702", "\uE313");
		utf2sb.put("1F528", "\uE116");
		utf2sb.put("1F4A1", "\uE10F");
		utf2sb.put("1F4F2", "\uE104");
		utf2sb.put("1F4E9", "\uE103");
		utf2sb.put("1F4EB", "\uE101");
		utf2sb.put("1F4EE", "\uE102");
		utf2sb.put("1F6C0", "\uE13F");
		utf2sb.put("1F6BD", "\uE140");
		utf2sb.put("1F4BA", "\uE11F");
		utf2sb.put("1F4B0", "\uE12F");
		utf2sb.put("1F531", "\uE031");
		utf2sb.put("1F6AC", "\uE30E");
		utf2sb.put("1F4A3", "\uE311");
		utf2sb.put("1F52B", "\uE113");
		utf2sb.put("1F48A", "\uE30F");
		utf2sb.put("1F489", "\uE13B");
		utf2sb.put("1F3C8", "\uE42B");
		utf2sb.put("1F3C0", "\uE42A");
		utf2sb.put("26BD", "\uE018");
		utf2sb.put("26BE", "\uE016");
		utf2sb.put("1F3BE", "\uE015");
		utf2sb.put("26F3", "\uE014");
		utf2sb.put("1F3B1", "\uE42C");
		utf2sb.put("1F3CA", "\uE42D");
		utf2sb.put("1F3C4", "\uE017");
		utf2sb.put("1F3BF", "\uE013");
		utf2sb.put("2660", "\uE20E");
		utf2sb.put("2665", "\uE20C");
		utf2sb.put("2663", "\uE20F");
		utf2sb.put("2666", "\uE20D");
		utf2sb.put("1F3C6", "\uE131");
		utf2sb.put("1F47E", "\uE12B");
		utf2sb.put("1F3AF", "\uE130");
		utf2sb.put("1F004", "\uE12D");
		utf2sb.put("1F3AC", "\uE324");
		utf2sb.put("1F4DD", "\uE301");
		utf2sb.put("1F4D6", "\uE148");
		utf2sb.put("1F3A8", "\uE502");
		utf2sb.put("1F3A4", "\uE03C");
		utf2sb.put("1F3A7", "\uE30A");
		utf2sb.put("1F3BA", "\uE042");
		utf2sb.put("1F3B7", "\uE040");
		utf2sb.put("1F3B8", "\uE041");
		utf2sb.put("303D", "\uE12C");
		utf2sb.put("1F45F", "\uE007");
		utf2sb.put("1F461", "\uE31A");
		utf2sb.put("1F460", "\uE13E");
		utf2sb.put("1F462", "\uE31B");
		utf2sb.put("1F455", "\uE006");
		utf2sb.put("1F454", "\uE302");
		utf2sb.put("1F457", "\uE319");
		utf2sb.put("1F458", "\uE321");
		utf2sb.put("1F459", "\uE322");
		utf2sb.put("1F380", "\uE314");
		utf2sb.put("1F3A9", "\uE503");
		utf2sb.put("1F451", "\uE10E");
		utf2sb.put("1F452", "\uE318");
		utf2sb.put("1F302", "\uE43C");
		utf2sb.put("1F4BC", "\uE11E");
		utf2sb.put("1F45C", "\uE323");
		utf2sb.put("1F484", "\uE31C");
		utf2sb.put("1F48D", "\uE034");
		utf2sb.put("1F48E", "\uE035");
		utf2sb.put("2615", "\uE045");
		utf2sb.put("1F375", "\uE338");
		utf2sb.put("1F37A", "\uE047");
		utf2sb.put("1F37B", "\uE30C");
		utf2sb.put("1F378", "\uE044");
		utf2sb.put("1F376", "\uE30B");
		utf2sb.put("1F374", "\uE043");
		utf2sb.put("1F354", "\uE120");
		utf2sb.put("1F35F", "\uE33B");
		utf2sb.put("1F35D", "\uE33F");
		utf2sb.put("1F35B", "\uE341");
		utf2sb.put("1F371", "\uE34C");
		utf2sb.put("1F363", "\uE344");
		utf2sb.put("1F359", "\uE342");
		utf2sb.put("1F358", "\uE33D");
		utf2sb.put("1F35A", "\uE33E");
		utf2sb.put("1F35C", "\uE340");
		utf2sb.put("1F372", "\uE34D");
		utf2sb.put("1F35E", "\uE339");
		utf2sb.put("1F373", "\uE147");
		utf2sb.put("1F362", "\uE343");
		utf2sb.put("1F361", "\uE33C");
		utf2sb.put("1F366", "\uE33A");
		utf2sb.put("1F367", "\uE43F");
		utf2sb.put("1F382", "\uE34B");
		utf2sb.put("1F370", "\uE046");
		utf2sb.put("1F34E", "\uE345");
		utf2sb.put("1F34A", "\uE346");
		utf2sb.put("1F349", "\uE348");
		utf2sb.put("1F353", "\uE347");
		utf2sb.put("1F346", "\uE34A");
		utf2sb.put("1F345", "\uE349");
		utf2sb.put("2600", "\uE04A");
		utf2sb.put("2614", "\uE04B");
		utf2sb.put("2601", "\uE049");
		utf2sb.put("26C4", "\uE048");
		utf2sb.put("1F319", "\uE04C");
		utf2sb.put("26A1", "\uE13D");
		utf2sb.put("1F300", "\uE443");
		utf2sb.put("1F30A", "\uE43E");
		utf2sb.put("1F431", "\uE04F");
		utf2sb.put("1F436", "\uE052");
		utf2sb.put("1F42D", "\uE053");
		utf2sb.put("1F439", "\uE524");
		utf2sb.put("1F430", "\uE52C");
		utf2sb.put("1F43A", "\uE52A");
		utf2sb.put("1F438", "\uE531");
		utf2sb.put("1F42F", "\uE050");
		utf2sb.put("1F428", "\uE527");
		utf2sb.put("1F43B", "\uE051");
		utf2sb.put("1F437", "\uE10B");
		utf2sb.put("1F42E", "\uE52B");
		utf2sb.put("1F417", "\uE52F");
		utf2sb.put("1F435", "\uE109");
		utf2sb.put("1F412", "\uE528");
		utf2sb.put("1F434", "\uE01A");
		utf2sb.put("1F40E", "\uE134");
		utf2sb.put("1F42B", "\uE530");
		utf2sb.put("1F411", "\uE529");
		utf2sb.put("1F418", "\uE526");
		utf2sb.put("1F40D", "\uE52D");
		utf2sb.put("1F426", "\uE521");
		utf2sb.put("1F424", "\uE523");
		utf2sb.put("1F414", "\uE52E");
		utf2sb.put("1F427", "\uE055");
		utf2sb.put("1F41B", "\uE525");
		utf2sb.put("1F419", "\uE10A");
		utf2sb.put("1F420", "\uE522");
		utf2sb.put("1F41F", "\uE019");
		utf2sb.put("1F433", "\uE054");
		utf2sb.put("1F42C", "\uE520");
		utf2sb.put("1F490", "\uE306");
		utf2sb.put("1F338", "\uE030");
		utf2sb.put("1F337", "\uE304");
		utf2sb.put("1F340", "\uE110");
		utf2sb.put("1F339", "\uE032");
		utf2sb.put("1F33B", "\uE305");
		utf2sb.put("1F33A", "\uE303");
		utf2sb.put("1F341", "\uE118");
		utf2sb.put("1F343", "\uE447");
		utf2sb.put("1F342", "\uE119");
		utf2sb.put("1F334", "\uE307");
		utf2sb.put("1F335", "\uE308");
		utf2sb.put("1F33E", "\uE444");
		utf2sb.put("1F41A", "\uE441");
		// U+20E3
		utf2sb.put("0031", "\uE21C");
		utf2sb.put("0032", "\uE21D");
		utf2sb.put("0033", "\uE21E");
		utf2sb.put("0034", "\uE21F");
		utf2sb.put("0035", "\uE220");
		utf2sb.put("0036", "\uE221");
		utf2sb.put("0037", "\uE222");
		utf2sb.put("0038", "\uE223");
		utf2sb.put("0039", "\uE224");
		utf2sb.put("0030", "\uE225");
		utf2sb.put("0023", "\uE210");

		utf2sb.put("2B06", "\uE232");
		utf2sb.put("2B07", "\uE233");
		utf2sb.put("2B05", "\uE235");
		utf2sb.put("27A1", "\uE234");
		utf2sb.put("2197", "\uE236");
		utf2sb.put("2196", "\uE237");
		utf2sb.put("2198", "\uE238");
		utf2sb.put("2199", "\uE239");
		utf2sb.put("25C0", "\uE23B");
		utf2sb.put("25B6", "\uE23A");
		utf2sb.put("23EA", "\uE23D");
		utf2sb.put("23E9", "\uE23C");
		utf2sb.put("1F197", "\uE24D");
		utf2sb.put("1F195", "\uE212");
		utf2sb.put("1F51D", "\uE24C");
		utf2sb.put("1F199", "\uE213");
		utf2sb.put("1F192", "\uE214");
		utf2sb.put("1F3A6", "\uE507");
		utf2sb.put("1F201", "\uE203");
		utf2sb.put("1F4F6", "\uE20B");
		utf2sb.put("1F235", "\uE22A");
		utf2sb.put("1F233", "\uE22B");
		utf2sb.put("1F250", "\uE226");
		utf2sb.put("1F239", "\uE227");
		utf2sb.put("1F22F", "\uE22C");
		utf2sb.put("1F23A", "\uE22D");
		utf2sb.put("1F236", "\uE215");
		utf2sb.put("1F21A", "\uE216");
		utf2sb.put("1F237", "\uE217");
		utf2sb.put("1F238", "\uE218");
		utf2sb.put("1F202", "\uE228");
		utf2sb.put("1F6BB", "\uE151");
		utf2sb.put("1F6B9", "\uE138");
		utf2sb.put("1F6BA", "\uE139");
		utf2sb.put("1F6BC", "\uE13A");
		utf2sb.put("1F6AD", "\uE208");
		utf2sb.put("1F17F", "\uE14F");
		utf2sb.put("267F", "\uE20A");
		utf2sb.put("1F687", "\uE434");
		utf2sb.put("1F6BE", "\uE309");
		utf2sb.put("3299", "\uE315");
		utf2sb.put("3297", "\uE30D");
		utf2sb.put("1F51E", "\uE207");
		utf2sb.put("1F194", "\uE229");
		utf2sb.put("2733", "\uE206");
		utf2sb.put("2734", "\uE205");
		utf2sb.put("1F49F", "\uE204");
		utf2sb.put("1F19A", "\uE12E");
		utf2sb.put("1F4F3", "\uE250");
		utf2sb.put("1F4F4", "\uE251");
		utf2sb.put("1F4B9", "\uE14A");
		utf2sb.put("1F4B1", "\uE149");
		utf2sb.put("2648", "\uE23F");
		utf2sb.put("2649", "\uE240");
		utf2sb.put("264A", "\uE241");
		utf2sb.put("264B", "\uE242");
		utf2sb.put("264C", "\uE243");
		utf2sb.put("264D", "\uE244");
		utf2sb.put("264E", "\uE245");
		utf2sb.put("264F", "\uE246");
		utf2sb.put("2650", "\uE247");
		utf2sb.put("2651", "\uE248");
		utf2sb.put("2652", "\uE249");
		utf2sb.put("2653", "\uE24A");
		utf2sb.put("26CE", "\uE24B");
		utf2sb.put("1F52F", "\uE23E");
		utf2sb.put("1F170", "\uE532");
		utf2sb.put("1F171", "\uE533");
		utf2sb.put("1F18E", "\uE534");
		utf2sb.put("1F17E", "\uE535");
		utf2sb.put("1F532", "\uE21A");
		utf2sb.put("1F534", "\uE219");
		utf2sb.put("1F533", "\uE21B");
		utf2sb.put("1F55B", "\uE02F");
		utf2sb.put("1F550", "\uE024");
		utf2sb.put("1F551", "\uE025");
		utf2sb.put("1F552", "\uE026");
		utf2sb.put("1F553", "\uE027");
		utf2sb.put("1F554", "\uE028");
		utf2sb.put("1F555", "\uE029");
		utf2sb.put("1F556", "\uE02A");
		utf2sb.put("1F557", "\uE02B");
		utf2sb.put("1F558", "\uE02C");
		utf2sb.put("1F559", "\uE02D");
		utf2sb.put("1F55A", "\uE02E");
		utf2sb.put("2B55", "\uE332");
		utf2sb.put("274C", "\uE333");
		utf2sb.put("00A9", "\uE24E");
		utf2sb.put("00AE", "\uE24F");
		utf2sb.put("2122", "\uE537");
		utf2sb.put("1F3E0", "\uE036");
		utf2sb.put("1F3EB", "\uE157");
		utf2sb.put("1F3E2", "\uE038");
		utf2sb.put("1F3E3", "\uE153");
		utf2sb.put("1F3E5", "\uE155");
		utf2sb.put("1F3E6", "\uE14D");
		utf2sb.put("1F3EA", "\uE156");
		utf2sb.put("1F3E9", "\uE501");
		utf2sb.put("1F3E8", "\uE158");
		utf2sb.put("1F492", "\uE43D");
		utf2sb.put("26EA", "\uE037");
		utf2sb.put("1F3EC", "\uE504");
		utf2sb.put("1F307", "\uE44A");
		utf2sb.put("1F306", "\uE146");
		utf2sb.put("1F3E7", "\uE154");
		utf2sb.put("1F3EF", "\uE505");
		utf2sb.put("1F3F0", "\uE506");
		utf2sb.put("26FA", "\uE122");
		utf2sb.put("1F3ED", "\uE508");
		utf2sb.put("1F5FC", "\uE509");
		utf2sb.put("1F5FB", "\uE03B");
		utf2sb.put("1F304", "\uE04D");
		utf2sb.put("1F305", "\uE449");
		utf2sb.put("1F303", "\uE44B");
		utf2sb.put("1F5FD", "\uE51D");
		utf2sb.put("1F308", "\uE44C");
		utf2sb.put("1F3A1", "\uE124");
		utf2sb.put("26F2", "\uE121");
		utf2sb.put("1F3A2", "\uE433");
		utf2sb.put("1F6A2", "\uE202");
		utf2sb.put("1F6A4", "\uE135");
		utf2sb.put("26F5", "\uE01C");
		utf2sb.put("2708", "\uE01D");
		utf2sb.put("1F680", "\uE10D");
		utf2sb.put("1F6B2", "\uE136");
		utf2sb.put("1F699", "\uE42E");
		utf2sb.put("1F697", "\uE01B");
		utf2sb.put("1F695", "\uE15A");
		utf2sb.put("1F68C", "\uE159");
		utf2sb.put("1F693", "\uE432");
		utf2sb.put("1F692", "\uE430");
		utf2sb.put("1F691", "\uE431");
		utf2sb.put("1F69A", "\uE42F");
		utf2sb.put("1F683", "\uE01E");
		utf2sb.put("1F689", "\uE039");
		utf2sb.put("1F684", "\uE435");
		utf2sb.put("1F685", "\uE01F");
		utf2sb.put("1F3AB", "\uE125");
		utf2sb.put("26FD", "\uE03A");
		utf2sb.put("1F6A5", "\uE14E");
		utf2sb.put("26A0", "\uE252");
		utf2sb.put("1F6A7", "\uE137");
		utf2sb.put("1F530", "\uE209");
		utf2sb.put("1F3B0", "\uE133");
		utf2sb.put("1F68F", "\uE150");
		utf2sb.put("1F488", "\uE320");
		utf2sb.put("2668", "\uE123");
		utf2sb.put("1F3C1", "\uE132");
		utf2sb.put("1F38C", "\uE143");
		utf2sb.put("1F1EF", "\uE50B");
		utf2sb.put("1F1F0", "\uE514");
		utf2sb.put("1F1E8", "\uE513");
		utf2sb.put("1F1FA", "\uE50C");
		utf2sb.put("1F1EB", "\uE50D");
		utf2sb.put("1F1EA", "\uE511");
		utf2sb.put("1F1EE", "\uE50F");
		utf2sb.put("1F1F7", "\uE512");
		utf2sb.put("1F1EC", "\uE510");
		utf2sb.put("1F1E9", "\uE50E");

		sb2utf.put("\uE415", "1F604");
		sb2utf.put("\uE056", "1F60A");
		sb2utf.put("\uE057", "1F603");
		sb2utf.put("\uE414", "263A");
		sb2utf.put("\uE405", "1F609");
		sb2utf.put("\uE106", "1F60D");
		sb2utf.put("\uE418", "1F618");
		sb2utf.put("\uE417", "1F61A");
		sb2utf.put("\uE40D", "1F633");
		sb2utf.put("\uE40A", "1F60C");
		sb2utf.put("\uE404", "1F601");
		sb2utf.put("\uE105", "1F61C");
		sb2utf.put("\uE409", "1F61D");
		sb2utf.put("\uE40E", "1F612");
		sb2utf.put("\uE402", "1F60F");
		sb2utf.put("\uE108", "1F613");
		sb2utf.put("\uE403", "1F614");
		sb2utf.put("\uE058", "1F61E");
		sb2utf.put("\uE407", "1F616");
		sb2utf.put("\uE401", "1F625");
		sb2utf.put("\uE40F", "1F630");
		sb2utf.put("\uE40B", "1F628");
		sb2utf.put("\uE406", "1F623");
		sb2utf.put("\uE413", "1F622");
		sb2utf.put("\uE411", "1F62D");
		sb2utf.put("\uE412", "1F602");
		sb2utf.put("\uE410", "1F632");
		sb2utf.put("\uE107", "1F631");
		sb2utf.put("\uE059", "1F620");
		sb2utf.put("\uE416", "1F621");
		sb2utf.put("\uE408", "1F62A");
		sb2utf.put("\uE40C", "1F637");
		sb2utf.put("\uE11A", "1F47F");
		sb2utf.put("\uE10C", "1F47D");
		sb2utf.put("\uE32C", "1F49B");
		sb2utf.put("\uE32A", "1F499");
		sb2utf.put("\uE32D", "1F49C");
		sb2utf.put("\uE328", "1F497");
		sb2utf.put("\uE32B", "1F49A");
		sb2utf.put("\uE022", "2764");
		sb2utf.put("\uE023", "1F494");
		sb2utf.put("\uE327", "1F493");
		sb2utf.put("\uE329", "1F498");
		sb2utf.put("\uE32E", "2728");
		sb2utf.put("\uE335", "1F31F");
		sb2utf.put("\uE334", "1F4A2");
		sb2utf.put("\uE337", "2755");
		sb2utf.put("\uE336", "2754");
		sb2utf.put("\uE13C", "1F4A4");
		sb2utf.put("\uE330", "1F4A8");
		sb2utf.put("\uE331", "1F4A6");
		sb2utf.put("\uE326", "1F3B6");
		sb2utf.put("\uE03E", "1F3B5");
		sb2utf.put("\uE11D", "1F525");
		sb2utf.put("\uE05A", "1F4A9");
		sb2utf.put("\uE00E", "1F44D");
		sb2utf.put("\uE421", "1F44E");
		sb2utf.put("\uE420", "1F44C");
		sb2utf.put("\uE00D", "1F44A");
		sb2utf.put("\uE010", "270A");
		sb2utf.put("\uE011", "270C");
		sb2utf.put("\uE41E", "1F44B");
		sb2utf.put("\uE012", "270B");
		sb2utf.put("\uE422", "1F450");
		sb2utf.put("\uE22E", "1F446");
		sb2utf.put("\uE22F", "1F447");
		sb2utf.put("\uE231", "1F449");
		sb2utf.put("\uE230", "1F448");
		sb2utf.put("\uE427", "1F64C");
		sb2utf.put("\uE41D", "1F64F");
		sb2utf.put("\uE00F", "261D");
		sb2utf.put("\uE41F", "1F44F");
		sb2utf.put("\uE14C", "1F4AA");
		sb2utf.put("\uE201", "1F6B6");
		sb2utf.put("\uE115", "1F3C3");
		sb2utf.put("\uE428", "1F46B");
		sb2utf.put("\uE51F", "1F483");
		sb2utf.put("\uE429", "1F46F");
		sb2utf.put("\uE424", "1F646");
		sb2utf.put("\uE423", "1F645");
		sb2utf.put("\uE253", "1F481");
		sb2utf.put("\uE426", "1F647");
		sb2utf.put("\uE111", "1F48F");
		sb2utf.put("\uE425", "1F491");
		sb2utf.put("\uE31E", "1F486");
		sb2utf.put("\uE31F", "1F487");
		sb2utf.put("\uE31D", "1F485");
		sb2utf.put("\uE001", "1F466");
		sb2utf.put("\uE002", "1F467");
		sb2utf.put("\uE005", "1F469");
		sb2utf.put("\uE004", "1F468");
		sb2utf.put("\uE51A", "1F476");
		sb2utf.put("\uE519", "1F475");
		sb2utf.put("\uE518", "1F474");
		sb2utf.put("\uE515", "1F471");
		sb2utf.put("\uE516", "1F472");
		sb2utf.put("\uE517", "1F473");
		sb2utf.put("\uE51B", "1F477");
		sb2utf.put("\uE152", "1F46E");
		sb2utf.put("\uE04E", "1F47C");
		sb2utf.put("\uE51C", "1F478");
		sb2utf.put("\uE51E", "1F482");
		sb2utf.put("\uE11C", "1F480");
		sb2utf.put("\uE536", "1F463");
		sb2utf.put("\uE003", "1F48B");
		sb2utf.put("\uE41C", "1F444");
		sb2utf.put("\uE41B", "1F442");
		sb2utf.put("\uE419", "1F440");
		sb2utf.put("\uE41A", "1F443");
		sb2utf.put("\uE436", "1F38D");
		sb2utf.put("\uE437", "1F49D");
		sb2utf.put("\uE438", "1F38E");
		sb2utf.put("\uE43A", "1F392");
		sb2utf.put("\uE439", "1F393");
		sb2utf.put("\uE43B", "1F38F");
		sb2utf.put("\uE117", "1F386");
		sb2utf.put("\uE440", "1F387");
		sb2utf.put("\uE442", "1F390");
		sb2utf.put("\uE446", "1F391");
		sb2utf.put("\uE445", "1F383");
		sb2utf.put("\uE11B", "1F47B");
		sb2utf.put("\uE448", "1F385");
		sb2utf.put("\uE033", "1F384");
		sb2utf.put("\uE112", "1F381");
		sb2utf.put("\uE325", "1F514");
		sb2utf.put("\uE312", "1F389");
		sb2utf.put("\uE310", "1F388");
		sb2utf.put("\uE126", "1F4BF");
		sb2utf.put("\uE127", "1F4C0");
		sb2utf.put("\uE008", "1F4F7");
		sb2utf.put("\uE03D", "1F3A5");
		sb2utf.put("\uE00C", "1F4BB");
		sb2utf.put("\uE12A", "1F4FA");
		sb2utf.put("\uE00A", "1F4F1");
		sb2utf.put("\uE00B", "1F4E0");
		sb2utf.put("\uE009", "260E");
		sb2utf.put("\uE316", "1F4BD");
		sb2utf.put("\uE129", "1F4FC");
		sb2utf.put("\uE141", "1F50A");
		sb2utf.put("\uE142", "1F4E2");
		sb2utf.put("\uE317", "1F4E3");
		sb2utf.put("\uE128", "1F4FB");
		sb2utf.put("\uE14B", "1F4E1");
		sb2utf.put("\uE211", "27BF");
		sb2utf.put("\uE114", "1F50D");
		sb2utf.put("\uE145", "1F513");
		sb2utf.put("\uE144", "1F512");
		sb2utf.put("\uE03F", "1F511");
		sb2utf.put("\uE313", "2702");
		sb2utf.put("\uE116", "1F528");
		sb2utf.put("\uE10F", "1F4A1");
		sb2utf.put("\uE104", "1F4F2");
		sb2utf.put("\uE103", "1F4E9");
		sb2utf.put("\uE101", "1F4EB");
		sb2utf.put("\uE102", "1F4EE");
		sb2utf.put("\uE13F", "1F6C0");
		sb2utf.put("\uE140", "1F6BD");
		sb2utf.put("\uE11F", "1F4BA");
		sb2utf.put("\uE12F", "1F4B0");
		sb2utf.put("\uE031", "1F531");
		sb2utf.put("\uE30E", "1F6AC");
		sb2utf.put("\uE311", "1F4A3");
		sb2utf.put("\uE113", "1F52B");
		sb2utf.put("\uE30F", "1F48A");
		sb2utf.put("\uE13B", "1F489");
		sb2utf.put("\uE42B", "1F3C8");
		sb2utf.put("\uE42A", "1F3C0");
		sb2utf.put("\uE018", "26BD");
		sb2utf.put("\uE016", "26BE");
		sb2utf.put("\uE015", "1F3BE");
		sb2utf.put("\uE014", "26F3");
		sb2utf.put("\uE42C", "1F3B1");
		sb2utf.put("\uE42D", "1F3CA");
		sb2utf.put("\uE017", "1F3C4");
		sb2utf.put("\uE013", "1F3BF");
		sb2utf.put("\uE20E", "2660");
		sb2utf.put("\uE20C", "2665");
		sb2utf.put("\uE20F", "2663");
		sb2utf.put("\uE20D", "2666");
		sb2utf.put("\uE131", "1F3C6");
		sb2utf.put("\uE12B", "1F47E");
		sb2utf.put("\uE130", "1F3AF");
		sb2utf.put("\uE12D", "1F004");
		sb2utf.put("\uE324", "1F3AC");
		sb2utf.put("\uE301", "1F4DD");
		sb2utf.put("\uE148", "1F4D6");
		sb2utf.put("\uE502", "1F3A8");
		sb2utf.put("\uE03C", "1F3A4");
		sb2utf.put("\uE30A", "1F3A7");
		sb2utf.put("\uE042", "1F3BA");
		sb2utf.put("\uE040", "1F3B7");
		sb2utf.put("\uE041", "1F3B8");
		sb2utf.put("\uE12C", "303D");
		sb2utf.put("\uE007", "1F45F");
		sb2utf.put("\uE31A", "1F461");
		sb2utf.put("\uE13E", "1F460");
		sb2utf.put("\uE31B", "1F462");
		sb2utf.put("\uE006", "1F455");
		sb2utf.put("\uE302", "1F454");
		sb2utf.put("\uE319", "1F457");
		sb2utf.put("\uE321", "1F458");
		sb2utf.put("\uE322", "1F459");
		sb2utf.put("\uE314", "1F380");
		sb2utf.put("\uE503", "1F3A9");
		sb2utf.put("\uE10E", "1F451");
		sb2utf.put("\uE318", "1F452");
		sb2utf.put("\uE43C", "1F302");
		sb2utf.put("\uE11E", "1F4BC");
		sb2utf.put("\uE323", "1F45C");
		sb2utf.put("\uE31C", "1F484");
		sb2utf.put("\uE034", "1F48D");
		sb2utf.put("\uE035", "1F48E");
		sb2utf.put("\uE045", "2615");
		sb2utf.put("\uE338", "1F375");
		sb2utf.put("\uE047", "1F37A");
		sb2utf.put("\uE30C", "1F37B");
		sb2utf.put("\uE044", "1F378");
		sb2utf.put("\uE30B", "1F376");
		sb2utf.put("\uE043", "1F374");
		sb2utf.put("\uE120", "1F354");
		sb2utf.put("\uE33B", "1F35F");
		sb2utf.put("\uE33F", "1F35D");
		sb2utf.put("\uE341", "1F35B");
		sb2utf.put("\uE34C", "1F371");
		sb2utf.put("\uE344", "1F363");
		sb2utf.put("\uE342", "1F359");
		sb2utf.put("\uE33D", "1F358");
		sb2utf.put("\uE33E", "1F35A");
		sb2utf.put("\uE340", "1F35C");
		sb2utf.put("\uE34D", "1F372");
		sb2utf.put("\uE339", "1F35E");
		sb2utf.put("\uE147", "1F373");
		sb2utf.put("\uE343", "1F362");
		sb2utf.put("\uE33C", "1F361");
		sb2utf.put("\uE33A", "1F366");
		sb2utf.put("\uE43F", "1F367");
		sb2utf.put("\uE34B", "1F382");
		sb2utf.put("\uE046", "1F370");
		sb2utf.put("\uE345", "1F34E");
		sb2utf.put("\uE346", "1F34A");
		sb2utf.put("\uE348", "1F349");
		sb2utf.put("\uE347", "1F353");
		sb2utf.put("\uE34A", "1F346");
		sb2utf.put("\uE349", "1F345");
		sb2utf.put("\uE04A", "2600");
		sb2utf.put("\uE04B", "2614");
		sb2utf.put("\uE049", "2601");
		sb2utf.put("\uE048", "26C4");
		sb2utf.put("\uE04C", "1F319");
		sb2utf.put("\uE13D", "26A1");
		sb2utf.put("\uE443", "1F300");
		sb2utf.put("\uE43E", "1F30A");
		sb2utf.put("\uE04F", "1F431");
		sb2utf.put("\uE052", "1F436");
		sb2utf.put("\uE053", "1F42D");
		sb2utf.put("\uE524", "1F439");
		sb2utf.put("\uE52C", "1F430");
		sb2utf.put("\uE52A", "1F43A");
		sb2utf.put("\uE531", "1F438");
		sb2utf.put("\uE050", "1F42F");
		sb2utf.put("\uE527", "1F428");
		sb2utf.put("\uE051", "1F43B");
		sb2utf.put("\uE10B", "1F437");
		sb2utf.put("\uE52B", "1F42E");
		sb2utf.put("\uE52F", "1F417");
		sb2utf.put("\uE109", "1F435");
		sb2utf.put("\uE528", "1F412");
		sb2utf.put("\uE01A", "1F434");
		sb2utf.put("\uE134", "1F40E");
		sb2utf.put("\uE530", "1F42B");
		sb2utf.put("\uE529", "1F411");
		sb2utf.put("\uE526", "1F418");
		sb2utf.put("\uE52D", "1F40D");
		sb2utf.put("\uE521", "1F426");
		sb2utf.put("\uE523", "1F424");
		sb2utf.put("\uE52E", "1F414");
		sb2utf.put("\uE055", "1F427");
		sb2utf.put("\uE525", "1F41B");
		sb2utf.put("\uE10A", "1F419");
		sb2utf.put("\uE522", "1F420");
		sb2utf.put("\uE019", "1F41F");
		sb2utf.put("\uE054", "1F433");
		sb2utf.put("\uE520", "1F42C");
		sb2utf.put("\uE306", "1F490");
		sb2utf.put("\uE030", "1F338");
		sb2utf.put("\uE304", "1F337");
		sb2utf.put("\uE110", "1F340");
		sb2utf.put("\uE032", "1F339");
		sb2utf.put("\uE305", "1F33B");
		sb2utf.put("\uE303", "1F33A");
		sb2utf.put("\uE118", "1F341");
		sb2utf.put("\uE447", "1F343");
		sb2utf.put("\uE119", "1F342");
		sb2utf.put("\uE307", "1F334");
		sb2utf.put("\uE308", "1F335");
		sb2utf.put("\uE444", "1F33E");
		sb2utf.put("\uE441", "1F41A");
		sb2utf.put("", "");
		sb2utf.put("\uE21C", "31");
		sb2utf.put("\uE21D", "32");
		sb2utf.put("\uE21E", "33");
		sb2utf.put("\uE21F", "34");
		sb2utf.put("\uE220", "35");
		sb2utf.put("\uE221", "36");
		sb2utf.put("\uE222", "37");
		sb2utf.put("\uE223", "38");
		sb2utf.put("\uE224", "39");
		sb2utf.put("\uE225", "30");
		sb2utf.put("\uE210", "23");
		sb2utf.put("", "");
		sb2utf.put("\uE232", "2B06");
		sb2utf.put("\uE233", "2B07");
		sb2utf.put("\uE235", "2B05");
		sb2utf.put("\uE234", "27A1");
		sb2utf.put("\uE236", "2197");
		sb2utf.put("\uE237", "2196");
		sb2utf.put("\uE238", "2198");
		sb2utf.put("\uE239", "2199");
		sb2utf.put("\uE23B", "25C0");
		sb2utf.put("\uE23A", "25B6");
		sb2utf.put("\uE23D", "23EA");
		sb2utf.put("\uE23C", "23000000000");
		sb2utf.put("\uE24D", "1F197");
		sb2utf.put("\uE212", "1F195");
		sb2utf.put("\uE24C", "1F51D");
		sb2utf.put("\uE213", "1F199");
		sb2utf.put("\uE214", "1F192");
		sb2utf.put("\uE507", "1F3A6");
		sb2utf.put("\uE203", "1F201");
		sb2utf.put("\uE20B", "1F4F6");
		sb2utf.put("\uE22A", "1F235");
		sb2utf.put("\uE22B", "1F233");
		sb2utf.put("\uE226", "1F250");
		sb2utf.put("\uE227", "1F239");
		sb2utf.put("\uE22C", "1F22F");
		sb2utf.put("\uE22D", "1F23A");
		sb2utf.put("\uE215", "1F236");
		sb2utf.put("\uE216", "1F21A");
		sb2utf.put("\uE217", "1F237");
		sb2utf.put("\uE218", "1F238");
		sb2utf.put("\uE228", "1F202");
		sb2utf.put("\uE151", "1F6BB");
		sb2utf.put("\uE138", "1F6B9");
		sb2utf.put("\uE139", "1F6BA");
		sb2utf.put("\uE13A", "1F6BC");
		sb2utf.put("\uE208", "1F6AD");
		sb2utf.put("\uE14F", "1F17F");
		sb2utf.put("\uE20A", "267F");
		sb2utf.put("\uE434", "1F687");
		sb2utf.put("\uE309", "1F6BE");
		sb2utf.put("\uE315", "3299");
		sb2utf.put("\uE30D", "3297");
		sb2utf.put("\uE207", "1F51E");
		sb2utf.put("\uE229", "1F194");
		sb2utf.put("\uE206", "2733");
		sb2utf.put("\uE205", "2734");
		sb2utf.put("\uE204", "1F49F");
		sb2utf.put("\uE12E", "1F19A");
		sb2utf.put("\uE250", "1F4F3");
		sb2utf.put("\uE251", "1F4F4");
		sb2utf.put("\uE14A", "1F4B9");
		sb2utf.put("\uE149", "1F4B1");
		sb2utf.put("\uE23F", "2648");
		sb2utf.put("\uE240", "2649");
		sb2utf.put("\uE241", "264A");
		sb2utf.put("\uE242", "264B");
		sb2utf.put("\uE243", "264C");
		sb2utf.put("\uE244", "264D");
		sb2utf.put("\uE245", "264E");
		sb2utf.put("\uE246", "264F");
		sb2utf.put("\uE247", "2650");
		sb2utf.put("\uE248", "2651");
		sb2utf.put("\uE249", "2652");
		sb2utf.put("\uE24A", "2653");
		sb2utf.put("\uE24B", "26CE");
		sb2utf.put("\uE23E", "1F52F");
		sb2utf.put("\uE532", "1F170");
		sb2utf.put("\uE533", "1F171");
		sb2utf.put("\uE534", "1F18E");
		sb2utf.put("\uE535", "1F17E");
		sb2utf.put("\uE21A", "1F532");
		sb2utf.put("\uE219", "1F534");
		sb2utf.put("\uE21B", "1F533");
		sb2utf.put("\uE02F", "1F55B");
		sb2utf.put("\uE024", "1F550");
		sb2utf.put("\uE025", "1F551");
		sb2utf.put("\uE026", "1F552");
		sb2utf.put("\uE027", "1F553");
		sb2utf.put("\uE028", "1F554");
		sb2utf.put("\uE029", "1F555");
		sb2utf.put("\uE02A", "1F556");
		sb2utf.put("\uE02B", "1F557");
		sb2utf.put("\uE02C", "1F558");
		sb2utf.put("\uE02D", "1F559");
		sb2utf.put("\uE02E", "1F55A");
		sb2utf.put("\uE332", "2B55");
		sb2utf.put("\uE333", "274C");
		sb2utf.put("\uE24E", "00A9");
		sb2utf.put("\uE24F", "00AE");
		sb2utf.put("\uE537", "2122");
		sb2utf.put("\uE036", "1F3E0");
		sb2utf.put("\uE157", "1F3EB");
		sb2utf.put("\uE038", "1F3E2");
		sb2utf.put("\uE153", "1F3E3");
		sb2utf.put("\uE155", "1F3E5");
		sb2utf.put("\uE14D", "1F3E6");
		sb2utf.put("\uE156", "1F3EA");
		sb2utf.put("\uE501", "1F3E9");
		sb2utf.put("\uE158", "1F3E8");
		sb2utf.put("\uE43D", "1F492");
		sb2utf.put("\uE037", "26EA");
		sb2utf.put("\uE504", "1F3EC");
		sb2utf.put("\uE44A", "1F307");
		sb2utf.put("\uE146", "1F306");
		sb2utf.put("\uE154", "1F3E7");
		sb2utf.put("\uE505", "1F3EF");
		sb2utf.put("\uE506", "1F3F0");
		sb2utf.put("\uE122", "26FA");
		sb2utf.put("\uE508", "1F3ED");
		sb2utf.put("\uE509", "1F5FC");
		sb2utf.put("\uE03B", "1F5FB");
		sb2utf.put("\uE04D", "1F304");
		sb2utf.put("\uE449", "1F305");
		sb2utf.put("\uE44B", "1F303");
		sb2utf.put("\uE51D", "1F5FD");
		sb2utf.put("\uE44C", "1F308");
		sb2utf.put("\uE124", "1F3A1");
		sb2utf.put("\uE121", "26F2");
		sb2utf.put("\uE433", "1F3A2");
		sb2utf.put("\uE202", "1F6A2");
		sb2utf.put("\uE135", "1F6A4");
		sb2utf.put("\uE01C", "26F5");
		sb2utf.put("\uE01D", "2708");
		sb2utf.put("\uE10D", "1F680");
		sb2utf.put("\uE136", "1F6B2");
		sb2utf.put("\uE42E", "1F699");
		sb2utf.put("\uE01B", "1F697");
		sb2utf.put("\uE15A", "1F695");
		sb2utf.put("\uE159", "1F68C");
		sb2utf.put("\uE432", "1F693");
		sb2utf.put("\uE430", "1F692");
		sb2utf.put("\uE431", "1F691");
		sb2utf.put("\uE42F", "1F69A");
		sb2utf.put("\uE01E", "1F683");
		sb2utf.put("\uE039", "1F689");
		sb2utf.put("\uE435", "1F684");
		sb2utf.put("\uE01F", "1F685");
		sb2utf.put("\uE125", "1F3AB");
		sb2utf.put("\uE03A", "26FD");
		sb2utf.put("\uE14E", "1F6A5");
		sb2utf.put("\uE252", "26A0");
		sb2utf.put("\uE137", "1F6A7");
		sb2utf.put("\uE209", "1F530");
		sb2utf.put("\uE133", "1F3B0");
		sb2utf.put("\uE150", "1F68F");
		sb2utf.put("\uE320", "1F488");
		sb2utf.put("\uE123", "2668");
		sb2utf.put("\uE132", "1F3C1");
		sb2utf.put("\uE143", "1F38C");
		sb2utf.put("\uE50B", "1F1EF");
		sb2utf.put("\uE514", "1F1F0");
		sb2utf.put("\uE513", "1F1E8");
		sb2utf.put("\uE50C", "1F1FA");
		sb2utf.put("\uE50D", "1F1EB");
		sb2utf.put("\uE511", "1F1EA");
		sb2utf.put("\uE50F", "1F1EE");
		sb2utf.put("\uE512", "1F1F7");
		sb2utf.put("\uE510", "1F1EC");
		sb2utf.put("\uE50E", "1F1E9");
	}

	public static String transUtf2sb(String code) {
		return utf2sb.get(code);
	}

	public static String transSb2utf(String code) {
		return sb2utf.get(code);
	}

	static Pattern p1 = Pattern.compile("<span([^/]*)</span>");
	static Pattern p2 = Pattern.compile("emoji(\\w+)");

	// public static void main(String[] args) {
	// System.out.println(formatUtf2sb("另一种暧昧<span class=\"emoji emoji1f4511\"></span><span class=\"emoji emoji1f451\"></span>"));
	//
	// System.out.println("2017-02-01".compareTo("2017-01-20"));
	// }

	// public static String formatUtf2sb(String name) {
	// String res = name;
	// Matcher m1, m2;
	// String span, code;
	// m1 = p1.matcher(res);
	// while(m1.find()) {
	// span = m1.group();
	// m2 = p2.matcher(span);
	// if(m2.find()) {
	// code = utf2sb.get(m2.group(1).toUpperCase());
	// if(code != null) {
	// res = res.replaceAll(span, code);
	// continue;
	// }
	// }
	// res = res.replaceAll(span, "[未知表情]");
	// }
	// return res.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]",
	// "*");
	// }
	//
	// public static String formatSb2utf(String name) {
	// String res = name;
	// Matcher m1, m2;
	// String span, code;
	// m1 = p1.matcher(res);
	// while(m1.find()) {
	// span = m1.group();
	// m2 = p2.matcher(span);
	// if(m2.find()) {
	// code = sb2utf.get(m2.group(1).toUpperCase());
	// if(code != null) {
	// res = res.replaceAll(span, code);
	// continue;
	// }
	// }
	// res = res.replaceAll(span, "[未知表情]");
	// }
	// return res.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]",
	// "*");
	// }

	/**
	 * 将str中的emoji表情转为byte数组
	 * 
	 * @param str
	 * @return
	 */
	public static String resolveToByteFromEmoji(String str) {
		str = str.replace("\\", "*");
		str = str.replaceAll("'", "\\\\'");
		Pattern pattern = Pattern
				.compile("[^(\u2E80-\u9FFF\\w\\s`~!@#\\$%\\^&\\*\\(\\)_+-？（）——=\\[\\]{}\\|;。，、《》”：；“！……’:'\"<,>\\.?/\\\\*)]");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb2 = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb2, resolveToByte(matcher.group(0)));
		}
		matcher.appendTail(sb2);
		return sb2.toString();
	}
	
	
	
	   public static Map<String,Object> resolveToByteFromMap(Map<String,Object> cond) {
	       Map<String,Object> map = new HashMap<>();
	       for (String key: cond.keySet()) {
	           if(cond.get(key).getClass().getName().equals("java.lang.String")) 
	               map.put(key, resolveToByteFromEmoji((String)cond.get(key)));
	           else 
	               map.put(key, cond.get(key));
	       }
	           return map;
	    }
	
	
	
	
	
	/**
	 * 将str中的byte数组类型的emoji表情前面加上两个换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String resolveEmojiByTxt(String str) {
		Pattern pattern = Pattern
				.compile("[^(\u2E80-\u9FFF\\w\\s`~!@#\\$%\\^&\\*\\(\\)_+-？（）——=\\[\\]{}\\|;。，、《》”：；“！……’:'\"<,>\\.?/\\\\*)]");
		Matcher matcher = pattern.matcher(str);
		String s = "";
		while (matcher.find()) {
			s = matcher.group(0);
			System.out.println(s);
			// str.replace(matcher.group(0), "/n/n"+matcher.group(0));
			// matcher.appendReplacement(sb2, "/n/n"+matcher.group(0));
		}
		str = str.replace(s, "\n\n" + s);
		return str;
	}

	public static String filterEmoji(String source) {
		if (source != null && source.length() > 0) {
			return source.replaceAll(
					"[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
		} else {
			return source;
		}
	}

	/**
	 * 将str中的byte数组类型的emoji表情转为正常显示的emoji表情
	 * 
	 * @param str
	 * @return
	 */
	public static String resolveToEmojiFromByte(String str) {
		Pattern pattern2 = Pattern.compile("<:([[-]\\d*[,]]+):>");
		Matcher matcher2 = pattern2.matcher(str);
		StringBuffer sb3 = new StringBuffer();
		while (matcher2.find()) {
			matcher2.appendReplacement(sb3, resolveToEmoji(matcher2.group(0)));
		}
		matcher2.appendTail(sb3);
		return sb3.toString();
	}
	

	private static String resolveToByte(String str) {
		byte[] b = str.getBytes();
		StringBuffer sb = new StringBuffer();
		sb.append("<:");
		for (int i = 0; i < b.length; i++) {
			if (i < b.length - 1) {
				sb.append(Byte.valueOf(b[i]).toString() + ",");
			} else {
				sb.append(Byte.valueOf(b[i]).toString());
			}
		}
		sb.append(":>");
		return sb.toString();
	}

	private static String resolveToEmoji(String str) {
		str = str.replaceAll("<:", "").replaceAll(":>", "");
		String[] s = str.split(",");
		byte[] b = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			b[i] = Byte.valueOf(s[i]);
		}
		return new String(b);
	}

}
