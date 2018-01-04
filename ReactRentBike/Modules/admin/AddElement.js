import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView, Alert, Picker} from 'react-native';
import Communications from 'react-native-communications';
import {ApiCalls} from "../ApiCalls";



class AddElement extends React.Component {
    static navigationOptions = {
    };
    constructor(props) {
        super(props);
        this.state = {street: "", numberOfBikes:"", numberOfAvailable:"", active:"Active", state:"created"};


    }

    create() {
        let rentbikeplace = this.state;
        for(let i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(rentbikeplace.street)===0){
                alert("Can't have two rent bike places on the same street!");
                return;
            }
        }

        if(isNaN(this.state.numberOfBikes) || isNaN(this.state.numberOfAvailable))
        {
            alert("Invalid input numbers!");
            return;
        }
        if(Number(this.state.numberOfAvailable) > Number(this.state.numberOfBikes))
        {
            alert("Number of available bikes can't be higher than number of bikes");
            return;
        }

        global.sync_controller.element_created(this.state, false);

        if(global.devicestate.localeCompare("online") === 0) {
            ApiCalls.add_rent_bike(this.state);
        }

        //global.sync.getAll();
        this.props.navigation.goBack();
    }

    render() {
        return (
            <ScrollView style={styles.container}>
                <Text>Street</Text>
                <TextInput style={styles.defaultTextInput} value={this.state.street.toString()} onChangeText={(street)=>this.setState({street})}/>
                <Text>Number of bikes</Text>
                <TextInput style={styles.defaultTextInput} value={this.state.numberOfBikes.toString()} onChangeText={(numberOfBikes)=>this.setState({numberOfBikes})}/>
                <Text>Number of available bikes</Text>
                <TextInput style={styles.defaultTextInput} value={this.state.numberOfAvailable.toString()} onChangeText={(numberOfAvailable)=>this.setState({numberOfAvailable})}/>
                <Picker selectedValue={this.state.active} onValueChange={(itemValue, itemIndex) => this.setState({active: itemValue})}>
                    <Picker.Item label="Active" value="Active" />
                    <Picker.Item label="Inactive" value="Inactive" />
                </Picker>
                <Button title="Create" onPress={()=>this.create()}/>
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

    },

    redButton: {
        backgroundColor: "#990000"
    }

});

export default AddElement;