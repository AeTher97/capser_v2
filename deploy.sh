#!/bin/bash

echo "Type commit message: "

read -r message;

cd capser_v2_frontend || exit
npm install
npm audit fix
npm run build
cd ..
cp -r capser_v2_frontend/build backend/static
git add -A
git commit -m "$message"
git push heroku master


