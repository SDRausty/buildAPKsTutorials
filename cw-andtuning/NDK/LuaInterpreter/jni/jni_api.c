/***
	Copyright (c) 2010 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

#include <stdlib.h>
#include <jni.h>
#include "jni_api.h"
#include "lua.h"
#include "lauxlib.h"
#include "lualib.h"

void *alloc(void *ud, void *ptr, size_t osize, size_t nsize)
	{
		if(nsize == 0)
		{
			free(ptr);
			return NULL;
		}

		return realloc(ptr, nsize);
	}
	
JNIEXPORT jstring JNICALL Java_com_commonsware_android_tuning_lua_LuaActivity_executeLua
  (JNIEnv *env, jclass clazz, jstring script) {
		jstring result;
		lua_State *L=lua_newstate(alloc, (void *)0);
		
		luaL_openlibs(L);
		
		const char *cscript=(*env)->GetStringUTFChars(env, script, NULL);
		
		int error=luaL_loadbuffer(L, cscript, strlen(cscript), "line") ||
										lua_pcall(L, 0, 1, 0);
				
		int t=lua_type(L, -1);
		
		switch (t) {
			case LUA_TSTRING: {
				result=(*env)->NewStringUTF(env, lua_tostring(L, -1));
				break;
			}
			case LUA_TBOOLEAN: {
				if (lua_toboolean(L, -1)) {
					result=(*env)->NewStringUTF(env, "true");
				}
				else {
					result=(*env)->NewStringUTF(env, "true");
				}
				break;
			}
			case LUA_TNUMBER: {
				char *cresult=malloc(64);
				sprintf(cresult, "%g", lua_tonumber(L, -1));
				result=(*env)->NewStringUTF(env, cresult);
				free(cresult);
				break;
			}
			default: {
				result=(*env)->NewStringUTF(env, lua_typename(L, -1));
				break;
			}
		}
		
		lua_pop(L, 1);
		lua_close(L);
		
		return(result);
}
