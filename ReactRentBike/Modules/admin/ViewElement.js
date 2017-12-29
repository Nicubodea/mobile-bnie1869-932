import React from 'react';
import { StyleSheet, Text, View, ScrollView, TextInput, Button, Navigator, ListView, Alert, Picker} from 'react-native';
import {Pie} from 'react-native-pathjs-charts';

class ViewElement extends React.Component {
    static navigationOptions = {
    };
    constructor(props) {
        super(props);
        this.state = {street: "", numberOfBikes:0, numberOfAvailable:0, active:"Active"};

        let current_element = this.props.navigation.state.params["data"];
        this.state.street = current_element.street;
        this.state.numberOfBikes = current_element.numberOfBikes;
        this.state.numberOfAvailable = current_element.numberOfAvailable;
        this.state.active = current_element.active;
    }

    edit() {
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

        let rentbikeplace = this.state;
        for(let i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(rentbikeplace.street)===0){
                global.rentbikeplaces[i].numberOfBikes = rentbikeplace.numberOfBikes;
                global.rentbikeplaces[i].numberOfAvailable = rentbikeplace.numberOfAvailable;
                global.rentbikeplaces[i].active = rentbikeplace.active;
            }
        }

        global.sync.editOne(rentbikeplace.street, rentbikeplace);

        global.vieewlist.update_callback();
        this.forceUpdate();
        this.props.navigation.goBack();
    }


    delete() {
        Alert.alert( 'Delete', 'Do you really want to delete this entry?', [ {text: 'No', onPress: () => console.log("Delete canceled")}, {text: 'Yes', onPress: () => this.really_delete()}, ], { cancelable: false } )
    }

    really_delete() {
        let rentbikeplace = this.state;
        for(let i =0;i<global.rentbikeplaces.length;i++){
            if(global.rentbikeplaces[i].street.localeCompare(rentbikeplace.street)===0){
                global.rentbikeplaces.splice(i, 1);
            }
        }

        global.vieewlist.update_callback();
        global.sync.removeOne(rentbikeplace.street);
        this.props.navigation.goBack();
    }

    render() {
        console.log(this.state.numberOfAvailable);
        console.log(this.state.numberOfBikes);
        const data = [
            {
                number:Number(this.state.numberOfBikes-this.state.numberOfAvailable+0.00005)

            },
            {
                number:Number(this.state.numberOfAvailable)
            }
        ];
        const sliceColor = ['#099A0F', '#990000'];

        let options = {
            margin: {
                top: 20,
                left: 20,
                right: 20,
                bottom: 20
            },
            width: 350,
            height: 350,
            color: '#000000',
            r: 50,
            R: 150,
            legendPosition: 'topLeft',
            animate: {
                type: 'oneByOne',
                duration: 200,
                fillTransition: 3
            },
            label: {
                fontFamily: 'Arial',
                fontSize: 8,
                fontWeight: true,
                color: '#ECF0F1'
            }
        };
        return (
            <ScrollView style={styles.container}>
                <Text style={styles.titleText}>{this.state.street}</Text>
                <Text>Number of bikes: </Text>

                <TextInput style={styles.defaultTextInput} value={this.state.numberOfBikes.toString()} onChangeText={(numberOfBikes)=>this.setState({numberOfBikes})}/>
                <Text>Number of available bikes: </Text>
                <TextInput style={styles.defaultTextInput} value={this.state.numberOfAvailable.toString()} onChangeText={(numberOfAvailable)=>this.setState({numberOfAvailable})}/>
                <Picker selectedValue={this.state.active} onValueChange={(itemValue, itemIndex) => this.setState({active: itemValue})}>
                    <Picker.Item label="Active" value="Active" />
                    <Picker.Item label="Inactive" value="Inactive" />
                </Picker>
                <Button title={"Edit"} onPress={()=>this.edit()}/>
                <Button title={"Delete"} onPress={()=>this.delete()} raised={true}/>
                <Pie
                    data={data}
                    accessorKey="number"
                    options={options}
                    pallete={
                        [
                            {'r':178,'g':34,'b':34},
                            {'r':34,'g':139,'b':34},
                        ]
                    }
                    />


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

export default ViewElement;