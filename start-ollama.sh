#!/bin/bash

MODEL_NAME=${OLLAMA_MODEL:-phi3} # Usa phi3 como padrão

echo "Iniciando o serviço Ollama em background..."
# Inicia o Ollama no background, para que o pull possa ser feito
ollama serve &
PID=$! # Captura o PID do processo em background

# Dá um pequeno tempo para o servidor subir
sleep 5

echo "Puxando o modelo: $MODEL_NAME"
# Tenta puxar o modelo
ollama pull $MODEL_NAME

# Se o pull falhar (o que pode acontecer em ambiente de pouco recurso), você pode ter problemas.
# Se for bem sucedido, o servidor continua rodando em background.

echo "Ollama está pronto para servir requisições."

# Espera o processo Ollama principal rodar indefinidamente
wait $PID