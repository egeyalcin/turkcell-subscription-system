brew install minikube

minikube start --driver=docker

minikube addons enable metrics-server

kubectl apply -f k8s/namespace.yaml

eval $(minikube docker-env)

mvn clean install

kubectl apply -f k8s/postgres/postgres-storage.yaml

kubectl apply -f k8s/postgres/postgres-deployments.yaml

kubectl apply -f k8s/postgres/postgres-services.yaml

kubectl apply -f k8s/zookeeper/
kubectl apply -f k8s/kafka/

docker build --build-arg JAR_FILE=subscription-service/target/*.jar -t turkcell-subscription-service .
docker build --build-arg JAR_FILE=payment-service/target/*.jar -t turkcell-payment-service .
docker build --build-arg JAR_FILE=turkcell-gateway-service/target/*.jar -t turkcell-gateway-service .
docker build --build-arg JAR_FILE=turkcell-gateway-service/target/*.jar -t turkcell-gateway-service .
docker build --build-arg JAR_FILE=config-server/target/*.jar -t config-server .

kubectl apply -f k8s/notification-service/ -n turkcell
kubectl apply -f k8s/payment-service/ -n turkcell
kubectl apply -f k8s/subscription-service/ -n turkcell

kubectl autoscale deployment subscription-service --cpu-percent=50 --min=2 --max=10 -n turkcell
kubectl autoscale deployment payment-service --cpu-percent=50 --min=2 --max=4 -n turkcell
kubectl autoscale deployment notification-service --cpu-percent=50 --min=2 --max=4 -n turkcell
kubectl autoscale deployment config-server --cpu-percent=50 --min=2 --max=5 -n turkcell
kubectl autoscale deployment turkcell-gateway-service --cpu-percent=50 --min=2 --max=5 -n turkcell