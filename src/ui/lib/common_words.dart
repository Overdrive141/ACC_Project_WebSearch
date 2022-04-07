import 'dart:math';

import 'package:flutter/material.dart';
import 'package:ui/api_service.dart';
import 'package:ui/main.dart';

final _api = APIService();

class CommonWords extends StatefulWidget {
  const CommonWords({Key? key}) : super(key: key);

  @override
  State<CommonWords> createState() => _CommonWordsState();
}

class _CommonWordsState extends State<CommonWords> {
  Map<String, int> _commonWords = {};
  Map<String, Map<String, List<String>>> _pattern = {};

  bool _isLoading = true;

  Map<String, List<String>> get phone => _pattern['phone'] ?? {};
  Map<String, List<String>> get email => _pattern['email'] ?? {};
  Map<String, List<String>> get url => _pattern['url'] ?? {};

  void _getCommonWords() async {
    _commonWords = Map<String, int>.from(await _api.getCommonWords());
    _commonWords = Map.fromEntries(
        _commonWords.entries.toList()..sort((a, b) => b.value - a.value));
    _pattern =
        Map<String, Map<String, List<String>>>.from(await _api.getPattern());
    setState(() {
      _isLoading = false;
    });
  }

  @override
  void initState() {
    _getCommonWords();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Analysis'),
        ),
        body:
            // show spinner if loading
            _isLoading
                ? const Center(
                    child: CircularProgressIndicator(),
                  )
                : SingleChildScrollView(
                    child: Padding(
                      padding: const EdgeInsets.all(40.0),
                      child: Wrap(
                        spacing: 24,
                        runSpacing: 24,
                        children: [
                          FractionallySizedBox(
                            widthFactor: 1,
                            child: AppCard(
                              child: Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Wrap(
                                  children: [
                                    const Header(title: 'Top 10 common words'),
                                    for (final word
                                        in _commonWords.entries.toList())
                                      FractionallySizedBox(
                                        widthFactor: 0.2,
                                        child: ListTile(
                                          leading: // circular avatar that shows the value
                                              CircleAvatar(
                                            backgroundColor: Colors.orange[500],
                                            child: Text(
                                              '${word.value}',
                                              style: const TextStyle(
                                                color: Colors.white,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                          ),
                                          title: Text(word.key),
                                          subtitle: Text('${word.value} times'),
                                        ),
                                      ),
                                  ],
                                ),
                              ),
                            ),
                          ),
                          PatternList(map: email, title: 'Emails found'),
                          PatternList(map: phone, title: 'Phone Numbers found'),
                          PatternList(map: url, title: 'URLs found'),
                        ],
                      ),
                    ),
                  ));
  }
}

class PatternList extends StatelessWidget {
  PatternList({
    Key? key,
    required this.map,
    required this.title,
  }) : super(key: key);

  final String title;
  final Map<String, List<String>> map;

  late final _color = _randomColor();

  // generate random color
  Color _randomColor() {
    return Color.fromARGB(
      255,
      Random.secure().nextInt(220),
      Random.secure().nextInt(220),
      Random.secure().nextInt(220),
    );
  }

  @override
  Widget build(BuildContext context) {
    return FractionallySizedBox(
      widthFactor: 0.32,
      child: AppCard(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Header(title: title),
              for (final word in map.entries.toList())
                ListTile(
                  leading: CircleAvatar(
                    backgroundColor: _color,
                    child: Text(
                      '${word.value.length}',
                      style: const TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  subtitle: Text(word.key),
                  title: Text(
                    word.value.join(", "),
                    // link styled text
                    style: const TextStyle(
                      color: Colors.blue,
                    ),
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}

class Header extends StatelessWidget {
  const Header({Key? key, required this.title}) : super(key: key);
  final String title;
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.all(12.0),
          child: Text(
            title,
            style: const TextStyle(
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        const Divider(height: 1),
      ],
    );
  }
}
