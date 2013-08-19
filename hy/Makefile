PLATFORM=$(shell uname)
ifeq ($(PLATFORM),Linux)
	VENV=/tmp/.venv-linux
endif
ifeq ($(PLATFORM),Darwin)
	VENV=/tmp/.venv-darwin
endif
ACT=$(VENV)/bin/activate

$(VENV):
	virtualenv $(VENV)

ubuntu-deps:
ifeq ($(shell which virtualenv),)
	sudo apt-get install python-virtualenv python-dev
endif

deps: $(VENV)
ifeq ($(PLATFORM),Linux)
	make ubuntu-deps
endif
	@. $(ACT) && pip install -r requirements.txt

clean-venv:
	rm -rf $(VENV)

clean:
	@echo

shell: deps
	@. $(ACT) && hy

clean-all: clean clean-venv

run: deps
	@. $(ACT) && hy starlanes/trader.hy
