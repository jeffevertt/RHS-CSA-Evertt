@echo off
set /p name="Enter Name: "
git config --global user.name "%name%"
set /p email="Enter Email: "
git config --global user.email "%email%"
@echo on