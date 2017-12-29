import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView} from 'react-native';
import Communications from 'react-native-communications';
import { ApiCalls } from "../ApiCalls";

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {username:"", password:"", shouldShowLogin: true};
        // at this point we should get the token and if we are already loggged in we should directly go to admin/user page
        // also verify the state here
        // if we are online, notify the user that they won't be able to login until they don't have internetz

    }


    login()
    {
        let promise = ApiCalls.login(this.state.username, this.state.password);
        promise.then((result) => {

            global.token = result["token"];

            ApiCalls.get_my_user(global.token).then((result) => {

                let user = JSON.parse(result["user"]);
                if(user["role"] === 0) {
                    this.props.navigation.navigate("UserPage");
                }
                else if(user["role"] === 1) {
                    this.props.navigation.navigate("AdminPage");
                }

            })


        });
    }

    render() {
        if(this.state.shouldShowLogin) {
            return (
                <ScrollView style={styles.container}>

                    <Text style={styles.titleText}>
                        Login
                    </Text>
                    <Text>
                        Username
                    </Text>
                    <TextInput style={styles.defaultTextInput} value={this.state.username}
                               onChangeText={(subject) => this.setState({username: subject, body: this.state.body})}>
                    </TextInput>
                    <Text>
                        Password
                    </Text>
                    <TextInput style={styles.defaultTextInput} password={true} value={this.state.password}
                               onChangeText={(body) => this.setState({username: this.state.username, password: body})}>
                    </TextInput>
                    <Button style={styles.defaultButton} title="Login" onPress={this.login.bind(this)}>
                    </Button>
                </ScrollView>
            );
        }
        else
        {
            return (<Text>You will be redirected in a second.</Text>);
        }
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

export default Login;