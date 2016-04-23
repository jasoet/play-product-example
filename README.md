Play Framework Rest API Example
=================================
![Travis](https://travis-ci.org/jasoet/play-product-example.svg?branch=master)

Please check `conf/routes` for available services.

## Requirement
* OracleJDK 8 or OpenJDK 8
* Docker Engine
* Docker Compose

## Building
* To build `./bin/activator clean compile`
* To run Dev Mode on local `./bin/activator clean compile run`
    - Set ENV Variable to connect Postgresql
* To generate docker image `./bin/activator docker:stage`.
    - Docker image will be generated in `/target/docker/stage`
    - There will be an `opt` directory and a `Dockerfile`
* To run test on local `./bin/activator clean test`
    - Set ENV Variable to connect Postgresql
    - ENV Variable for Running and Testing are different

## ENV Variable
* Main
   - APPLICATION_SECRET=<Secret>
   - PLAY_PRODUCT_DB_URL=jdbc:postgresql://localhost:5432/product
   - PLAY_PRODUCT_DB_USERNAME=root
   - PLAY_PRODUCT_DB_PASSWORD=localhost
* Test
   - PLAY_PRODUCT_DB_TEST_URL=jdbc:postgresql://localhost:5432/product-test
   - PLAY_PRODUCT_DB_TEST_USERNAME=root
   - PLAY_PRODUCT_DB_TEST_PASSWORD=localhost

## Production
- Build and run as production mode, there are two helper script available in `/deployment`
- Every Script need to run inside `/deployment`
- `build-install.sh` build and copy docker image to `/deployment/app`
- `deploy-prod.sh` build docker image and run in production mode

## Deploy on Digital Ocean using Docker Machine
- Create machine with [Digital Ocean driver](https://docs.docker.com/machine/drivers/digital-ocean/)
```
docker-machine create \
	--driver digitalocean \
	--digitalocean-access-token=<Do Access Token> \
	--digitalocean-region=sgp1 \
	--digitalocean-size=1gb \
	--digitalocean-ssh-key-fingerprint=<DO SSH Key Fingerprint> \
	do-docker
```
- Check if machine is created `docker-machine ls`
- Connect local Docker Client to Docker Machine `eval $(docker-machine env do-docker)`
- Deploy Application