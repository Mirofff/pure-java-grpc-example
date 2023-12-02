package com.github.mirofff.app;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.main.product.Product;
import io.grpc.main.product.ProductId;
import io.grpc.main.product.ProductList;
import io.grpc.main.product.ProductServiceGrpc.ProductServiceImplBase;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductInfoImpl extends ProductServiceImplBase {
  private Map<String, Product> productMap = new HashMap<String, Product>();

  @Override
  public void addProduct(Product request, StreamObserver<ProductId> responseObserver) {
    UUID uuid = UUID.randomUUID();

    String randomUUIDString = uuid.toString();

    productMap.put(randomUUIDString, request);

    ProductId id = ProductId.newBuilder().setId(randomUUIDString).build();

    responseObserver.onNext(id);
    responseObserver.onCompleted();
  }

  @Override
  public void getProduct(ProductId request, StreamObserver<Product> responseObserver) {
    String id = request.getId();
    if (productMap.containsKey(id)) {
      responseObserver.onNext((Product) productMap.get(id));

      responseObserver.onCompleted();
    } else {
      responseObserver.onError(new StatusException(Status.NOT_FOUND));
    }
  }

  @Override
  public void allProducts(Empty request, StreamObserver<ProductList> responseObserver) {
    ProductList productList = ProductList.newBuilder().addAllProducts(productMap.values()).build();
    responseObserver.onNext(productList);
    responseObserver.onCompleted();
  }
}
