package com.api.deployer.ui.bindings.render.properties;

import java.net.URI;
import java.net.URISyntaxException;


import com.redshape.bindings.types.IBindable;
import com.redshape.ui.UIException;
import com.redshape.ui.bindings.properties.IPropertyUI;

import javax.swing.*;

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
