// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: productdto.proto

package com.grpc.user.product;

public final class Productdto {
  private Productdto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Category_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Category_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Product_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Product_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020productdto.proto\"G\n\010Category\022\022\n\ncatego" +
      "ryId\030\001 \001(\t\022\024\n\014categoryName\030\002 \001(\t\022\021\n\timag" +
      "ePath\030\003 \001(\t\"\266\001\n\007Product\022\021\n\tproductId\030\001 \001" +
      "(\t\022\033\n\010category\030\002 \001(\0132\t.Category\022\023\n\013produ" +
      "ctName\030\003 \001(\t\022\024\n\014productPrice\030\004 \001(\t\022\032\n\022pr" +
      "oductDescription\030\005 \001(\t\022\021\n\tviewCount\030\006 \001(" +
      "\005\022\021\n\timagePath\030\007 \001(\t\022\016\n\006userId\030\010 \001(\tB\031\n\025" +
      "com.grpc.user.productP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_Category_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Category_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Category_descriptor,
        new java.lang.String[] { "CategoryId", "CategoryName", "ImagePath", });
    internal_static_Product_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_Product_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Product_descriptor,
        new java.lang.String[] { "ProductId", "Category", "ProductName", "ProductPrice", "ProductDescription", "ViewCount", "ImagePath", "UserId", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}