package com.example.tolek.player.jaudiotagger.tag.asf;

import com.example.tolek.player.jaudiotagger.audio.asf.data.ContentBranding;

import com.example.tolek.player.jaudiotagger.audio.asf.data.ContainerType;
import com.example.tolek.player.jaudiotagger.audio.asf.data.MetadataDescriptor;

/**
 * This field represents the image content of the banner image which is stored
 * in the {@linkplain ContentBranding content branding} chunk of ASF files.<br>
 * 
 * @author Christian Laireiter
 */
public class AsfTagBannerField extends AbstractAsfTagImageField
{

    /**
     * Creates an instance with no image data.<br>
     */
    public AsfTagBannerField() {
        super(AsfFieldKey.BANNER_IMAGE);
    }

    /**
     * Creates an instance with given descriptor as image content.<br>
     * 
     * @param descriptor
     *            image content.
     */
    public AsfTagBannerField(final MetadataDescriptor descriptor) {
        super(descriptor);
        assert descriptor.getName().equals(
                AsfFieldKey.BANNER_IMAGE.getFieldName());
    }

    /**
     * Creates an instance with specified data as image content.
     * 
     * @param imageData
     *            image content.
     */
    public AsfTagBannerField(final byte[] imageData) {
        super(new MetadataDescriptor(ContainerType.CONTENT_BRANDING,
                AsfFieldKey.BANNER_IMAGE.getFieldName(),
                MetadataDescriptor.TYPE_BINARY));
        this.toWrap.setBinaryValue(imageData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getImageDataSize() {
        return this.toWrap.getRawDataSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getRawImageData() {
        return getRawContent();
    }

}