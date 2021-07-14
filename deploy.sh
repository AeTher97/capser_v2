#!/bin/bash

cd frontend || exit
npm install
npm audit fix
CI='' npm run build
cd ..
cp -r frontend/build/. backend/src/main/resources/static
mkdir temp
cp -r backend/. temp/
cp .netrc ~/

HEROKU_API_TOKEN=$1

cd temp || exit


git config user.email "michael93509@gmail.com"
git config user.name "Michal Wozniak"
git init
git add .
git commit -m "Deploy"



git push -f https://heroku:${HEROKU_API_TOKEN}@git.heroku.com/capser.git master
cd ..
rm -r -f temp

echo "Deploy successful"

