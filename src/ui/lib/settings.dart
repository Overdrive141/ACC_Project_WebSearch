import 'dart:async';

import 'package:flutter/material.dart';
import 'package:ui/main.dart';

import 'api_service.dart';

class Settings extends StatefulWidget {
  const Settings({Key? key}) : super(key: key);

  @override
  State<Settings> createState() => _SettingsState();
}

class _SettingsState extends State<Settings> {
  bool isRunning = false;

  final _textController = TextEditingController();
  final _api = APIService();

  Timer? _timer;

  String? errorText;

  void buildIndex(String value) {
    if (value.isEmpty) {
      errorText = 'Please enter a valid url';
      setState(() {});
      return;
    }
    errorText = null;
    isRunning = true;
    setState(() {});
    _api.build(value);
    checkStatus();
    isRunning = false;
    setState(() {});
  }

  Future<void> resetIndex() async {
    isRunning = true;
    setState(() {});
    await _api.reset();
    isRunning = false;
    setState(() {});
  }

  // fucntion to check status
  Future<void> checkStatus() async {
    // periodic timer to check status
    // const oneSec = Duration(seconds: 1);
    // _timer = Timer.periodic(oneSec, (Timer t) async {
    //   final status = await _api.checkStatus();
    //   if (status == false) {
    //     _timer?.cancel();
    //   }
    //   setState(() {
    //     isRunning = status;
    //   });
    // });
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Settings'),
        ),
        body: Padding(
          padding: const EdgeInsets.all(40.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              AppCard(
                child: TextField(
                  controller: _textController,
                  enabled: !isRunning,
                  decoration: InputDecoration(
                    border: InputBorder.none,
                    prefixIcon: const Icon(Icons.link),
                    hintText: 'Enter urls here (separated by comma)...',
                    labelText: 'URLs',
                    errorText: errorText,
                  ),
                  onSubmitted: buildIndex,
                  style: const TextStyle(height: 2),
                ),
              ),
              const SizedBox(height: 20),
              Row(
                children: [
                  ElevatedButton(
                    child: const Text('Build Index'),
                    onPressed: isRunning
                        ? null
                        : () => buildIndex(_textController.text),
                  ),
                  const SizedBox(width: 20),
                  OutlinedButton(
                    child: const Text(
                      'Reset Index',
                      style: TextStyle(color: Colors.red),
                    ),
                    onPressed: resetIndex,
                  ),
                ],
              ),
            ],
          ),
        ));
  }
}
