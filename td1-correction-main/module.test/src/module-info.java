module module.test {
	requires module.helpers;
	requires module.lib;
	// requis pour pouvoir utiliser
	// la reflection depuis le module `lib`
	// comment faire autrement ?!?
	opens test;
}