// Dio api service class
import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';

class APIService {
  static const baseUrl = "http://localhost:7070/";
  late Dio _dio;

  APIService() {
    BaseOptions options = BaseOptions(
      baseUrl: baseUrl,
      connectTimeout: 50000,
      receiveTimeout: 120000,
    );
    _dio = Dio(options);
  }

  Future<Map<String, int>> getCommonWords() async {
    try {
      final response = await _dio.get('/common');
      final data = json.decode(response.data);
      return Map<String, int>.from(data);
    } on DioError catch (e) {
      handleError(e);
    }
    return {};
  }

  Future<Map<String, Map<String, List<String>>>> getPattern() async {
    try {
      final response = await _dio.get('/pattern');
      Map data = json.decode(response.data);
      print(data);
      data = data as Map<String, dynamic>;
      Map<String, Map<String, dynamic>> x = data.map((key, val) {
        return MapEntry(key, Map<String, dynamic>.from(val));
      });
      Map<String, Map<String, List<String>>> y = x.map((key, val) {
        return MapEntry(
            key,
            val.map((key, val) =>
                MapEntry<String, List<String>>(key, List<String>.from(val))));
      });
      return y;
    } on DioError catch (e) {
      handleError(e);
    }
    return {};
  }

  Future<void> build(String urls) async {
    try {
      await _dio.post('/build',
          data: FormData.fromMap({
            'urls': urls,
          }));
    } on DioError catch (e) {
      handleError(e);
    }
  }

  Future<bool> checkStatus() async {
    try {
      final res = await _dio.get('/status');
      return res.statusCode == 200;
    } on DioError catch (e) {
      handleError(e);
    }
    return false;
  }

  Future<void> reset() async {
    try {
      await _dio.post('/reset');
    } on DioError catch (e) {
      handleError(e);
    }
  }

  Future<Map<String, dynamic>> getResults(String query) async {
    try {
      final response = await _dio.post('/search',
          data: FormData.fromMap({
            'Search_Query': query,
          }));
      final data = json.decode(response.data);
      return data as Map<String, dynamic>;
    } on DioError catch (e) {
      handleError(e);
    }
    return {};
  }

  Future<List<String>> getAutocomplete(String query) async {
    // return empty list if null
    if (query.isEmpty) {
      return const <String>[];
    }
    try {
      final response = await _dio.post('/complete',
          data: FormData.fromMap({
            'query': query,
          }));
      final data = json.decode(response.data)['data'];
      return List<String>.from(data);
    } on DioError catch (e) {
      handleError(e);
    }
    return [];
  }

  void handleError<T>(DioError e) {
    debugPrint(e.toString());
  }
}
