package com.api.deployer.ui.bindings.render.properties;

import javax.swing.*;

import com.redshape.bindings.types.IBindable;
import com.redshape.ui.application.UIException;
import com.redshape.ui.data.bindings.properties.IPropertyUI;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;

public class URIPropertyUI extends JTextField implements IPropertyUI<URI> {
    private IBindable descriptor;

	public URIPropertyUI( IBindable bindable ) {
		this.descriptor = bindable;
	}

    @Override
    public void setValue(URI value) throws UIException {
        this.setText( value.toString() );
    }

    @Override
    public URI getValue() throws UIException {
        try {
            return new URI( this.getText() );
        } catch ( URISyntaxException e ) {
            throw new UIException( e.getMessage(), e );
        }
    }

    @Override
    public JComponent asComponent() {
        return this;
    }
}
