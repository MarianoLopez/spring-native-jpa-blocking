install-sdkman:
	curl -s "https://get.sdkman.io" | bash
	source "$HOME/.sdkman/bin/sdkman-init.sh"
	sdk version

install-dependencies:
	sdk install java 21.0.0.2.r11-grl
	gu install native-image

build-native:
	mvn -Pnative-image package

