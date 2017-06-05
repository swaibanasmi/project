from flask import Flask,jsonify,request

app = Flask(__name__)

languages=[ {'name' : 'javascript',} , {'name' : 'ruby'} , {'name' : 'python'} ,{'name' : 'javascript',} ]

@app.route('/lang', methods=['GET'])
def returnall():
	return jsonify({'languages' : languages})

@app.route('/lang' , methods=['POST'])
def addone():
	language = {'name': request.json['name']}
	languages.append(language)
	return jsonify({'languages' : languages})

@app.route('/lang/<string:name>' , methods=['PUT'])
def updateone(name):
	langs = [ language for language in languages if language['name']==name]
	langs[0]['name']=request.json['name']
	langs[1]['name']=request.json['name']
	return jsonify({'languages' : languages})

@app.route('/lang/<string:name>' , methods=['DELETE'])
def removeone(name):
	langs = [ language for language in languages if language['name']==name]
	languages.remove(langs[0])
	return jsonify({'languages' : languages})


if __name__ == '__main__':
	app.run(debug=True , port=8080)