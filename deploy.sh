#!/bin/bash

cd frontend || exit
mkdir node_modules
sudo chown -R $USER "./node_modules"
sudo yarn install
CI='' sudo yarn run build
cd ..
cp -r frontend/build/. backend/src/main/resources/static
mkdir temp
cp -r backend/. temp/
cp .netrc ~/

HEROKU_API_TOKEN=$1

cd temp || exit

git init
git config user.email "michael93509@gmail.com"
git config user.name "Michal Wozniak"
git add .
git commit -m $2

if git push -f https://heroku:${HEROKU_API_TOKEN}@git.heroku.com/capser.git master; then
  echo "Push successful"
else
  echo "Git push failed"
  exit 125
fi

cd ..
rm -r -f temp

echo "Deploy successful"
