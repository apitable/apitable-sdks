import { IGetNodeListResponseData, IGetNodeListReqParams, IGetNodeDetailReqParams, IGetNodeDetailResponseData } from './interface';
import { APITable } from './apitable';

export class NodeManager {
  apitable: APITable

  constructor(apitable: APITable) {
    this.apitable = apitable;
  }
  /**
   * Get the list of root nodes of the specified space stations.
   * @param param0 
   */
  async list<T = IGetNodeListResponseData>(params: IGetNodeListReqParams) {
    return this.apitable.request<T>({
      path: `/spaces/${params.spaceId}/nodes`,
      method: 'get',
    });
  }
  /**
   * Get information about the specified node.
   */
  async get<T = IGetNodeDetailResponseData>(params: IGetNodeDetailReqParams) {
    return this.apitable.request<T>({
      path: `/spaces/${params.spaceId}/nodes/${params.nodeId}`,
      method: 'get',
    });
  }
}