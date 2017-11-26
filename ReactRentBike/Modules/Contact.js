import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView} from 'react-native';
import Communications from 'react-native-communications';

class Contact extends React.Component {
    static navigationOptions = {
        tabBarLabel: 'Contact',

    };



    constructor(props) {
        super(props);
        this.state = {subject:"", body:""};
    }

    send_mail() {
        Communications.email(["nicubodea96@gmail.com"],null, null, this.state.subject, this.state.body);

    }

    render() {
        return (
            <ScrollView style={styles.container}>
                <Text style={styles.titleText}>
                    Contact
                </Text>
                <Text>
                    Subject
                </Text>
                <TextInput style={styles.defaultTextInput} value={this.state.subject} onChangeText={(subject)=>this.setState({subject:subject, body:this.state.body})}>
                </TextInput>
                <Text>
                    Message
                </Text>
                <TextInput style={styles.defaultMultiLine} multiline={true} value={this.state.body} onChangeText={(body)=>this.setState({subject:this.state.subject, body:body})}>
                </TextInput>
                <Button style={styles.defaultButton} title="Send" onPress={this.send_mail.bind(this)}>
                </Button>
            </ScrollView>
        );
    }
}

const styles = StyleSheet.create({
    navbar: {
        flex:1,
        flexDirection:"row",
        justifyContent: "space-around"
    },
    container: {
        paddingVertical: 60
    },
    titleText: {
        fontSize:48
    },
    defaultTextInput: {
        height:40
    },
    defaultMultiLine: {
        height: 250,
        textAlignVertical: 'top'
    },
    defaultButtonNav: {

    }
});

export default Contact;