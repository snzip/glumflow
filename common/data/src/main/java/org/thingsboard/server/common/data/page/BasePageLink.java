package org.thingsboard.server.common.data.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
public abstract class BasePageLink implements Serializable {

    private static final long serialVersionUID = -4189954843653250481L;

    @Getter protected final int limit;

    @Getter @Setter protected UUID idOffset;

}
