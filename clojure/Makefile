STANDALONE=target/starlanes-0.1.0-SNAPSHOT-standalone.jar

build:
	@lein compile
	@lein uberjar

clean:
	@echo

shell:
	@lein repl

clean-all: clean clean-venv

run: build
	#@lein exec src/starlanes/trader.clj
	java -jar $(STANDALONE)

check:
	@lein kibit
	@lein test