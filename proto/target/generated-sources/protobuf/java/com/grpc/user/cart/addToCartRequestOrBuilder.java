// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: cart-service.proto

package com.grpc.user.cart;

public interface addToCartRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:addToCartRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string productId = 1;</code>
   * @return The productId.
   */
  java.lang.String getProductId();
  /**
   * <code>string productId = 1;</code>
   * @return The bytes for productId.
   */
  com.google.protobuf.ByteString
      getProductIdBytes();

  /**
   * <code>string productName = 2;</code>
   * @return The productName.
   */
  java.lang.String getProductName();
  /**
   * <code>string productName = 2;</code>
   * @return The bytes for productName.
   */
  com.google.protobuf.ByteString
      getProductNameBytes();

  /**
   * <code>string productPrice = 3;</code>
   * @return The productPrice.
   */
  java.lang.String getProductPrice();
  /**
   * <code>string productPrice = 3;</code>
   * @return The bytes for productPrice.
   */
  com.google.protobuf.ByteString
      getProductPriceBytes();

  /**
   * <code>string productDescription = 5;</code>
   * @return The productDescription.
   */
  java.lang.String getProductDescription();
  /**
   * <code>string productDescription = 5;</code>
   * @return The bytes for productDescription.
   */
  com.google.protobuf.ByteString
      getProductDescriptionBytes();

  /**
   * <code>int32 viewCount = 6;</code>
   * @return The viewCount.
   */
  int getViewCount();

  /**
   * <code>int32 totalProducts = 7;</code>
   * @return The totalProducts.
   */
  int getTotalProducts();

  /**
   * <code>string userId = 8;</code>
   * @return The userId.
   */
  java.lang.String getUserId();
  /**
   * <code>string userId = 8;</code>
   * @return The bytes for userId.
   */
  com.google.protobuf.ByteString
      getUserIdBytes();
}