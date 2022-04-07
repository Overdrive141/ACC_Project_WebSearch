import 'package:easy_autocomplete/easy_autocomplete.dart';
import 'package:flutter/material.dart';
import 'package:ui/common_words.dart';
import 'package:ui/settings.dart';
import 'package:url_launcher/url_launcher.dart';

import 'api_service.dart';

void main() {
  runApp(const MyApp());
}

final _api = APIService();

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Search Engine',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Web Search Engine'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final _textController = TextEditingController();
  Map<String, dynamic> _results = {};
  bool init = false;

  List<String> get suggestions {
    final data = Map<String, int>.from(_results['suggestions'] ?? {});
    return Map.fromEntries(
            data.entries.toList()..sort((a, b) => a.value - b.value))
        .keys
        .toList();
  }

  int get numResults => results.length;
  int get resultTime => _results['time']?['time'] ?? 0;

  Map<String, int> get results {
    return Map<String, int>.from(_results['searchResults'] ?? {});
  }

  Future<List<String>> _fetchSuggestions(String searchValue) async {
    if (searchValue.isEmpty) {
      return const <String>[];
    }
    final values = _api.getAutocomplete(searchValue);
    return values;
  }

  Future _fetchResults(String searchValue) async {
    if (searchValue.isEmpty) {
      return const <String>[];
    }
    if (init != true) {
      init = true;
    }
    _results = await _api.getResults(searchValue);
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        actions: [
          IconButton(
            icon: const Icon(Icons.info),
            onPressed: () => Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => const CommonWords(),
              ),
            ),
          ),
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () => Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => const Settings(),
              ),
            ),
          ),
          const SizedBox(width: 16),
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            const SizedBox(height: 30),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 30.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  AppCard(
                    child: EasyAutocomplete(
                        decoration: const InputDecoration(
                          border: InputBorder.none,
                          prefixIcon: Icon(Icons.search),
                          hintText: 'Search for keyword here...',
                        ),
                        onSubmitted: _fetchResults,
                        controller: _textController,
                        debounceDuration: const Duration(milliseconds: 300),
                        asyncSuggestions: _fetchSuggestions,
                        suggestionBuilder: (word) {
                          return ListTile(
                            dense: true,
                            title: Text(word),
                          );
                        }),
                  ),
                  const SizedBox(height: 20),
                  if (suggestions.isNotEmpty)
                    Row(
                      children: [
                        const Text(
                          'Did you mean: ',
                          style: TextStyle(
                            color: Colors.black87,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        for (String word in suggestions)
                          Padding(
                            padding: const EdgeInsets.only(right: 6.0),
                            child: GestureDetector(
                              onTap: () {
                                _textController.text = word;
                                _fetchResults(word);
                              },
                              child: Text(
                                word,
                                style: const TextStyle(
                                  color: Colors.blue,
                                ),
                              ),
                            ),
                          ),
                      ],
                    ),
                  const SizedBox(height: 20),
                ],
              ),
            ),
            Divider(
              color: Colors.grey[300],
              thickness: 1,
              height: 1,
            ),
            if (results.entries.isNotEmpty)
              Padding(
                padding: const EdgeInsets.only(
                    top: 20.0, bottom: 30.0, left: 30.0, right: 30.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Found $numResults results in ${resultTime / 1000} ms',
                      style: const TextStyle(
                        fontSize: 16,
                        color: Colors.black45,
                      ),
                    ),
                    const SizedBox(height: 20),
                    AppCard(
                      child: Column(
                        children: [
                          for (final x in results.entries) ...[
                            ResultCard(
                              subtitle: x.value,
                              title: x.key,
                            ),
                            Divider(
                              color: Colors.grey[100],
                              thickness: 1,
                              height: 1,
                            ),
                          ]
                        ],
                      ),
                    ),
                  ],
                ),
              )
            else if (!init) ...[
              const SizedBox(height: 100),
              const Text(
                'Search for a keyword to get results...',
                style: TextStyle(
                  color: Colors.black26,
                  fontWeight: FontWeight.bold,
                ),
              )
            ] else ...[
              const SizedBox(height: 100),
              const Text(
                'No results',
                style: TextStyle(
                  fontSize: 18,
                  color: Colors.black38,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const Text(
                'Please try another keyword',
                style: TextStyle(
                  color: Colors.black38,
                ),
              ),
            ]
          ],
        ),
      ),
    );
  }
}

class ResultCard extends StatelessWidget {
  const ResultCard({
    Key? key,
    required this.title,
    required this.subtitle,
  }) : super(key: key);

  final String title;
  final int subtitle;

  void _launchUrl() async {
    if (await canLaunch(title)) {
      await launch(title);
    } else {
      throw 'Could not launch $title';
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: CircleAvatar(
          backgroundColor: Colors.blue[400],
          child: Text(
            subtitle.toString(),
            style: const TextStyle(
                color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold),
          )),
      title: Text(title,
          style: const TextStyle(
            decoration: TextDecoration.underline,
            fontWeight: FontWeight.bold,
            color: Colors.blue,
            // color: Colors.blue[900],
          )),
      onTap: _launchUrl,
      subtitle: Text("Keyword occurs $subtitle times"),
    );
  }
}

class AppCard extends StatelessWidget {
  const AppCard({
    Key? key,
    required this.child,
  }) : super(key: key);

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Container(
        decoration: BoxDecoration(
          // border radius
          borderRadius: const BorderRadius.all(Radius.circular(3.0)),
          color: Colors.white,
          boxShadow: [
            BoxShadow(
              color: Colors.grey.withOpacity(0.3),
              blurRadius: 10.0,
              spreadRadius: 7.0,
              offset: const Offset(
                5.0,
                5.0,
              ),
            ),
          ],
        ),
        child: child);
  }
}
