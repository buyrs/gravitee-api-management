/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.debug.handlers.api;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import io.gravitee.el.TemplateVariableProvider;
import io.gravitee.gateway.core.component.ComponentProvider;
import io.gravitee.gateway.core.endpoint.ref.impl.DefaultReferenceRegister;
import io.gravitee.gateway.debug.reactor.handler.context.DebugExecutionContextFactory;
import io.gravitee.gateway.handlers.api.ApiReactorHandlerFactory;
import io.gravitee.gateway.handlers.api.context.ApiTemplateVariableProvider;
import io.gravitee.gateway.handlers.api.definition.Api;
import io.gravitee.gateway.reactor.handler.context.ApiTemplateVariableProviderFactory;
import io.gravitee.gateway.reactor.handler.context.DefaultV3ExecutionContextFactory;
import io.gravitee.gateway.reactor.handler.context.V3ExecutionContextFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.xml.*" })
@PrepareForTest({ DefaultV3ExecutionContextFactory.class, ApiReactorHandlerFactory.class })
public class DebugApiContextHandlerFactoryTest {

    @InjectMocks
    private DebugApiReactorHandlerFactory debugApiContextHandlerFactory;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ComponentProvider componentProvider;

    @Mock
    private DefaultReferenceRegister referenceRegister;

    @Test
    public void building_v3ExecutionContextFactory_should_put_ApiTemplateVariableProvider_in_context() throws Exception {
        when(applicationContext.getBean(ApiTemplateVariableProviderFactory.class))
            .thenReturn(mock(ApiTemplateVariableProviderFactory.class));
        when(applicationContext.getBean(ApiTemplateVariableProviderFactory.class).getTemplateVariableProviders())
            .thenReturn(new ArrayList<>());

        whenNew(DefaultV3ExecutionContextFactory.class).withAnyArguments().thenReturn(mock(DefaultV3ExecutionContextFactory.class));

        V3ExecutionContextFactory executionContextFactory = debugApiContextHandlerFactory.v3ExecutionContextFactory(
            mock(Api.class),
            componentProvider,
            referenceRegister
        );

        assertTrue(executionContextFactory instanceof DebugExecutionContextFactory);

        ArgumentCaptor<List<TemplateVariableProvider>> providersCaptor = ArgumentCaptor.forClass(List.class);
        verifyNew(DefaultV3ExecutionContextFactory.class).withArguments(same(componentProvider), providersCaptor.capture());
        assertTrue(providersCaptor.getValue().contains(referenceRegister));
        assertTrue(providersCaptor.getValue().stream().filter(ApiTemplateVariableProvider.class::isInstance).findAny().isPresent());
    }
}
