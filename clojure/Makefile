STANDALONE=target/starlanes-0.1.0-SNAPSHOT-standalone.jar

build:
	@lein compile
	@lein uberjar

clean:
	rm -rf target

shell:
	@lein repl

run: build
	#@lein exec src/starlanes/trader.clj
	java -jar $(STANDALONE)

test-only:
	@lein test

check:
	@lein kibit
	@lein test