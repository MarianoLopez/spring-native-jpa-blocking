install-sdkman:
	curl -s "https://get.sdkman.io" | bash
	source "$HOME/.sdkman/bin/sdkman-init.sh"
	sdk version

install-dependencies:
	sdk install java 21.0.0.2.r11-grl
	gu install native-image

build-native:
	mvn -Pnative-image package -DskipTests

run:
	mvn spring-boot:run

run-native:
	./target/com.z.nativejpablocking.nativejpablockingapplication

run-tests:
	mvn test -f pom.xml

run-tests-with-report:
	mvn surefire-report:report

