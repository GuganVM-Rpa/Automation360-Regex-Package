package com.automationanywhere.botcommands;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "Extract", label = "[[Extract.label]]",
        node_label = "[[Extract.node_label]]", description = "[[Extract.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[Extract.return_label]]", return_type = STRING, return_required = true, return_description = "[[Extract.return_label_description]]")

public class Extract {
    //Messages read from full qualified property file name and provide i18n capability.
    private static final Messages MESSAGES = MessagesFactory
            .getMessages("com.automationanywhere.botcommand.samples.messages");

    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute

    public <string> Value<String> action(
            //Idx 1 would be displayed first, with a text box for entering the value.
            @Idx(index = "1", type = TEXT)
            //UI labels.
            @Pkg(label = "[[Extract.Input.label]]")
            //Ensure that a validation error is thrown when the value is null.
            @NotEmpty
                    String Input,

            @Idx(index = "2", type = TEXT)
            @Pkg(label = "[[Extract.Regex.label]]")
            @NotEmpty
                    String Regex) {
        //Internal validation, to disallow empty strings. No null check needed as we have NotEmpty on firstString.
        if ("".equals(Input.trim()))
            throw new BotCommandException(MESSAGES.getString("emptyInputString", "Input"));

        if ("".equals(Regex.trim()))
            throw new BotCommandException(MESSAGES.getString("emptyInputString", "Regex"));

        //Business logic
        String result;
        String pattern;
        try {
            Pattern r = Pattern.compile(Regex);
            Matcher m = r.matcher(Input);
            if(m.find()){
                result =  m.group(0);
            }else {
                result = "";
            }
        }catch (Exception e){
            throw new BotCommandException("There was an issue extracting the text from" + Input +".Full Exception Text:" + e.toString());
        }
        //Return StringValue.
        return new StringValue(result);
    }
}
