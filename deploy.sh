#!/bin/bash

cd capser_v2_frontend || exit
npm install
npm audit fix
CI='' npm run build
cd ..
cp -r capser_v2_frontend/build/. backend/src/main/resources/static
mkdir temp
cp -r backend/. temp/
cp .netrc ~/


cd temp || exit
git init
git add .
git commit -m "Deploy"


git config user.email "michael93509@gmail.com"
git config user.name "Michal Wozniak"

git push -f https://heroku:${HEROKU_API_TOKEN}@git.heroku.com/capser.git origin/master:master
cd ..
rm -r -f temp

echo "Deploy successful"

