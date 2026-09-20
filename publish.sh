#!/usr/bin/zsh

# Environment variables
export $(xargs < .env)

# Modrinth
gradleww :26.2:modrinth
gradleww :26.3:modrinth

# CurseForge
gradleww :26.2:publishCurseforge
gradleww :26.3:publishCurseforge

gradleww :26.3:publishGithub
