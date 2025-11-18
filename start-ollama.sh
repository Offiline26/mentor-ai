#!/bin/bash

# Puxa o modelo especificado na variável de ambiente
echo "Puxando o modelo Ollama: $OLLAMA_MODEL"
ollama pull $OLLAMA_MODEL

# Inicia o serviço Ollama. O --host 0.0.0.0 garante que seja acessível
echo "Iniciando o serviço Ollama na porta 11434"
ollama serve