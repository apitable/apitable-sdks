
# Makefile for convinient code run on local development

.PHONY: install
install:
	cd apitable.js && yarn
	cd apitable.py && make install
	cd apitable.java && mvn --batch-mode --update-snapshots verify

.PHONY: test
test:
	cd apitable.js && yarn test:coverage
	cd apitable.py && make test
	cd apitable.java && mvn --batch-mode test -Dmaven.test.skip=false
