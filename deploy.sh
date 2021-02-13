#!/bin/bash

apt-get install git
apt-get install heroku
apt-get install expect




cd capser_v2_frontend || exit
npm install
npm audit fix
CI='' npm run build
cd ..
cp -r capser_v2_frontend/build/. backend/src/main/resources/static
mkdir temp
cp -r backend/. temp/
ls
expect heroku_login.ex
cd temp || exit
git init
git add .
git commit -m "Deploy"

heroku git:remote -a capser
git push heroku master -f
cd ..
rm -r -f temp

echo "Deploy successful"

